package SwitchAnalyzer.Network;

import SwitchAnalyzer.Database.DBFrame;
import SwitchAnalyzer.Kafka.GenericConsumer;
import SwitchAnalyzer.Kafka.GenericProducer;
import SwitchAnalyzer.Kafka.Producer;
import SwitchAnalyzer.Kafka.Topics;
import SwitchAnalyzer.MainHandler_Master;
import SwitchAnalyzer.NamingConventions;
import SwitchAnalyzer.Network.ErrorDetection.CRC;
import SwitchAnalyzer.Network.ErrorDetection.ErrorDetectingAlgorithms;
import SwitchAnalyzer.miscellaneous.GlobalVariable;
import SwitchAnalyzer.miscellaneous.JSONConverter;
import SwitchAnalyzer.miscellaneous.Time;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.pcap4j.core.PacketListener;
import org.pcap4j.core.PcapHandle;
import org.pcap4j.packet.EthernetPacket;
import org.pcap4j.packet.Packet;

import java.util.HashMap;


public class FrameProcessing
{
    static GenericConsumer consumer = new GenericConsumer(IP.ip1 + ":" + Ports.port1, "frameProcessing_1", true);
    public static GenericProducer packetProducer = new GenericProducer(IP.ip1, true);
    public static ErrorDetectingAlgorithms errorDetectingAlgorithms = null;
    public static Producer cmdProducer = new Producer(IP.ip1);
    public static HashMap<String, String> countMap = new HashMap<>();

    public static DBFrame processFrames(byte[] frameBytes) {
        DBFrame frameResult = new DBFrame();
        frameResult.setCrcChecker(new CRC().isAlgorithmCorrect(frameBytes));
        if (frameResult.errorInCrcCheckerExists())
        {
            long count = Integer.getInteger(countMap.get(NamingConventions.crcError)) + 1;
            countMap.put(NamingConventions.crcError, String.valueOf(count));
        }
        EthernetPacket packet = null;
        try { packet = EthernetPacket.newPacket(frameBytes, 0, frameBytes.length); }
        catch (Exception e) { e.printStackTrace(); }
        System.out.println(packet);
        HashMap<String, String> map = new HashMap<>();
        Packet.Builder builder = packet.getBuilder();
        do
        {
            Packet p = builder.build();
            if(p.getHeader() == null) break;
            map.put(getType(p.getHeader()), bytesToString(p.getRawData()));
            builder = builder.getPayloadBuilder();
        }
        while(builder != null);
        if(builder != null)
            map.put("Payload", bytesToString(builder.build().getRawData()));
        frameResult.frameData = map;
        return frameResult;
    }

    private static String bytesToString (byte[] networkHeaderBytes)
    {
        StringBuilder sb = new StringBuilder();
        for (byte networkHeaderByte : networkHeaderBytes) {
            sb.append(String.format("%02X", networkHeaderByte)).append(" ");
        }
        return sb.toString();
    }

    public static void startProcessFrames()
    {
        if (errorDetectingAlgorithms instanceof CRC)
            countMap.put(NamingConventions.crcError, "0");
        countMap.put(NamingConventions.totalPacketCount, "0");
        consumer.selectTopicByteArray(Topics.FramesFromHPC);
        while (!GlobalVariable.stopRecieving) { consumeFrames(); }
        clear();
    }

    private static void clear()
    {
        countMap.clear();
        errorDetectingAlgorithms = null;
    }

    private static void consumeFrames()
    {
        ConsumerRecords<String, byte[]> frames = consumer.consumeByteArray(Time.waitTime);
        for (ConsumerRecord<String, byte[]> frame : frames)
        {
            DBFrame dbFrame = processFrames(frame.value());
            String json = JSONConverter.toJSON(dbFrame);
            packetProducer.send(Topics.ProcessedFramesFromHPC, json);
            MainHandler_Master.storages.get(GlobalVariable.storageClass).store(dbFrame);
            countMap.put(NamingConventions.totalPacketCount, String.valueOf(Long.parseLong(countMap.get("TotalCount"))+1));
        }
    }

    public static String getType(Packet.Header header)
    {
        if (header.getRawData().length != 0) {
            String headerString = header.toString();
            StringBuilder result = new StringBuilder();
            int i = 0;
            while (headerString.charAt(i) != ']')
            {
                char c = headerString.charAt(i++);
                if (c == '[')
                    continue;
                if (c == '(' || c == ' ')
                    break;
                result.append(c);
            }
            countMap.putIfAbsent("XXX" + result, "0");
            long count = Integer.getInteger(countMap.get("XXX" + result)) + 1;
            countMap.put("XXX" + result, String.valueOf(count));
            return result.toString();
        }
        return "";
    }

    public static void main(String[] args)
    {
        PCAP.initialize();
        PcapHandle handle = PCAP.createHandle();
        PacketListener listener =
                pcapPacket ->
                {
                    System.out.println(pcapPacket);
                    processFrames(pcapPacket.getRawData());
                };
        try { handle.loop(1,listener); }
        catch (Exception e) { e.printStackTrace(); }
    }
}
