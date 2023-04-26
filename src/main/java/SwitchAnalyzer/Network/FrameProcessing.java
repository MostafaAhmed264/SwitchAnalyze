package SwitchAnalyzer.Network;

import SwitchAnalyzer.Database.DBFrame;
import SwitchAnalyzer.Database.Headers_Data;
import SwitchAnalyzer.Kafka.GenericConsumer;
import SwitchAnalyzer.Kafka.GenericProducer;
import SwitchAnalyzer.Kafka.Producer;
import SwitchAnalyzer.Kafka.Topics;
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
    static GenericConsumer consumer = new GenericConsumer(IP.ip1 + ":" + Ports.port1, "asdadsafhfhsfasbzx", true);
    public static GenericProducer packetProducer = new GenericProducer(IP.ip1, true);
    public static ErrorDetectingAlgorithms errorDetectingAlgorithms = null;
    public static Producer cmdProducer = new Producer(IP.ip1);

    public static DBFrame processFrames(byte[] frameBytes) {
        DBFrame frameResult = new DBFrame();
        frameResult.setCrcChecker(new CRC().isAlgorithmCorrect(frameBytes));
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
        }while(builder != null);
        if(builder != null)
            map.put("Payload", bytesToString(builder.build().getRawData()));
        frameResult.frameData = map;
        // set the port
        // set the directionEnum in DBFrame then set direction according to it
        // set error in routing
        System.out.println("ASDSADDASADSDS");
        System.out.println(frameResult.frameData.keySet().toArray()[0]);
        return frameResult;
    }

    private static String bytesToString (byte[] networkHeaderBytes)
    {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < networkHeaderBytes.length; i++)
        { sb.append(String.format("%02X", networkHeaderBytes[i])).append(" "); }
        return sb.toString();
    }

    public static void startProcessFrames()
    {
        consumer.selectTopicByteArray(Topics.FramesFromHPC);
        while (!GlobalVariable.stopRecieving) { consumeFrames(); }
    }

    private static void consumeFrames()
    {
        ConsumerRecords<String, byte[]> frames = consumer.consumeByteArray(Time.waitTime);
        for (ConsumerRecord<String, byte[]> frame : frames)
        {
            DBFrame dbFrame = processFrames(frame.value());
            String json = JSONConverter.toJSON(dbFrame);
            cmdProducer.produce(json, Topics.ProcessedFramesFromHPC);
            new Headers_Data().store(dbFrame);
            //MainHandler_Master.storages.get(GlobalVariable.storageClass).store(dbFrame);
        }
    }

    public static String getType(Packet.Header header)
    {
        if (header.getRawData().length != 0) {
            String headerString = header.toString();
            StringBuilder result = new StringBuilder("");
            int i = 0;
            while (headerString.charAt(i) != ']') {
                char c = headerString.charAt(i++);
                if (c == '[')
                    continue;
                if (c == '(')
                    break;
                result.append(c);
            }
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
