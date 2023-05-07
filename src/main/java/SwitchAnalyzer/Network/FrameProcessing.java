package SwitchAnalyzer.Network;

import SwitchAnalyzer.Database.Frame_DB;
import SwitchAnalyzer.Kafka.GenericConsumer;
import SwitchAnalyzer.Kafka.Producer;
import SwitchAnalyzer.Kafka.Topics;
import SwitchAnalyzer.MainHandler_Master;
import SwitchAnalyzer.NamingConventions;
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

import java.math.BigInteger;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import static org.pcap4j.util.ByteArrays.calcCrc32Checksum;


public class FrameProcessing
{
    static GenericConsumer consumer = new GenericConsumer(IP.ip1 + ":" + Ports.port1, "framePtxryugfggfugodsddasdaijisgvffvxzzvceffssdsfsing_11111", true);
    public static Producer packetProducer = new Producer(IP.ip1);
    public static ErrorDetectingAlgorithms errorDetectingAlgorithms = null;
    public static ConcurrentHashMap<String, String> countMap = new ConcurrentHashMap<>();

    static Boolean  checkCRC(byte[] frameBytes, HashMap<String , String > map)
    {
        byte[] slicedArray = Arrays.copyOfRange(frameBytes, 0, frameBytes.length - 4);
        byte[] crc = Arrays.copyOfRange(frameBytes, frameBytes.length-4, frameBytes.length);
        map.put("CRC FIELD", bytesToString(crc));
        return Arrays.equals(BigInteger.valueOf(Integer.reverseBytes(calcCrc32Checksum(slicedArray))).toByteArray(), crc);
    }

    static void extractHeaders(byte[] frameBytes, HashMap<String , String > map)
    {
        EthernetPacket packet = null;
        //create Pacekt
        try { packet = EthernetPacket.newPacket(frameBytes, 0, frameBytes.length); }
        catch (Exception e) { e.printStackTrace(); }
        //count the packet
        long count = Integer.parseInt(countMap.get(NamingConventions.totalPacketCount)) + 1;
        countMap.put(NamingConventions.totalPacketCount, String.valueOf(count));
        //Extract Headers.
        Packet.Builder builder = packet.getBuilder();
        do
        {
            Packet p = builder.build();
            if(p.getHeader() == null) break;
            map.put(getType(p.getHeader()) ,(bytesToString(p.getHeader().getRawData())));
            builder = builder.getPayloadBuilder();
        }
        while(builder != null);
        //Extract Payload
        if (builder != null) { map.put("Payload", bytesToString(builder.build().getRawData())); }
    }
    public static Frame_DB processFrames(byte[] frameBytes)
    {
        HashMap<String , String > frameDetails = new HashMap<>();
        Frame_DB frameResult = new Frame_DB();
        frameResult.setCrcChecker(!checkCRC(frameBytes, frameDetails));
        if(frameResult.errorInCrcCheckerExists()) {
            long count = Integer.parseInt(countMap.get(NamingConventions.crcError)) + 1;
            countMap.put(NamingConventions.crcError, String.valueOf(count)) ;
        }

        extractHeaders(frameBytes, frameDetails);
        frameResult.frameDetails = frameDetails;

        return frameResult;
    }

    private static String bytesToString (byte[] networkHeaderBytes)
    {
        StringBuilder sb = new StringBuilder();
        for (byte networkHeaderByte : networkHeaderBytes) { sb.append(String.format("%02X", networkHeaderByte)).append(" "); }
        return sb.toString();
    }

    public static void startProcessFrames()
    {
        countMap.putIfAbsent(NamingConventions.crcError, "0");
        countMap.putIfAbsent(NamingConventions.totalPacketCount, "0");
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
            Frame_DB dbFrame = processFrames(frame.value());
            MainHandler_Master.storages.get(GlobalVariable.storageClass).store(dbFrame);
            if(GlobalVariable.retreiveProcessedFramesFromHPC)
            {
                dbFrame.bytes = bytesToString(frame.value());
                dbFrame.Direction = "In";
                String json = JSONConverter.toJSON(dbFrame);
                packetProducer.produce(json, Topics.ProcessedFramesFromHPC);
            }
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
            long count = Long.parseLong(countMap.get("XXX" + result)) + 1;
            countMap.put("XXX" + result, String.valueOf(count));
            return result.toString();
        }
        return "";
    }

    public static void main(String[] args)
    {
        PCAP.initialize();
        PcapHandle handle = PCAP.createHandle();
        try { handle.setDirection(PcapHandle.PcapDirection.IN); }
        catch (Exception ignored) {}
        PacketListener listener =
                pcapPacket ->
                {
                    processFrames(pcapPacket.getRawData());
                };
        try { handle.loop(-1,listener); }
        catch (Exception e) { e.printStackTrace(); }
    }
}