package SwitchAnalyzer.Network;

import SwitchAnalyzer.Database.DBFrame;
import SwitchAnalyzer.Kafka.GenericConsumer;
import SwitchAnalyzer.Kafka.Producer;
import SwitchAnalyzer.Kafka.Topics;
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

import java.math.BigInteger;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import static org.pcap4j.util.ByteArrays.calcCrc32Checksum;


public class FrameProcessing
{
    static GenericConsumer consumer = new GenericConsumer(IP.ip1 + ":" + Ports.port1, "framePxrocessing_1111", true);
    public static Producer packetProducer = new Producer(IP.ip1);
    public static ErrorDetectingAlgorithms errorDetectingAlgorithms = null;
    public static ConcurrentHashMap<String, String> countMap = new ConcurrentHashMap<>();

    static Boolean  checkCRC(byte[] frameBytes, HashMap<String , String > map)
    {
        byte[] slicedArray = Arrays.copyOfRange(frameBytes, 0, frameBytes.length - 4);
        byte[] crc = Arrays.copyOfRange(frameBytes, frameBytes.length-4, frameBytes.length);
        System.out.println(bytesToString(crc));
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
        System.out.println(packet);
        //Extract Headers.
        Packet.Builder builder = packet.getBuilder();
        do
        {
            Packet p = builder.build();
            if(p.getHeader() == null) break;
            System.out.println(p.getHeader());
            map.put(getType(p.getHeader()) ,(bytesToString(p.getHeader().getRawData())));
            builder = builder.getPayloadBuilder();
        }
        while(builder != null);
        //Extract Payload
        if (builder != null) { map.put("Payload", bytesToString(builder.build().getRawData())); }
    }
    public static DBFrame processFrames(byte[] frameBytes)
    {
        HashMap<String , String > frameDetails = new HashMap<>();
        DBFrame frameResult = new DBFrame();
        frameResult.setCrcChecker(!checkCRC(frameBytes, frameDetails));
        if(frameResult.errorInCrcCheckerExists()) {
            long count = Integer.parseInt(countMap.get(NamingConventions.crcError)) + 1;
            countMap.put(NamingConventions.crcError, String.valueOf(count)) ;
        }

        extractHeaders(frameBytes, frameDetails);
        frameResult.frameData = frameDetails;

        System.out.println(frameResult.frameData);
        System.out.println(countMap);
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
            DBFrame dbFrame = processFrames(frame.value());
            String json = JSONConverter.toJSON(dbFrame);
            System.out.println(json);
            packetProducer.produce(json, Topics.ProcessedFramesFromHPC);
            //MainHandler_Master.storages.get(GlobalVariable.storageClass).store(dbFrame);
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
                    System.out.println(pcapPacket);
                    processFrames(pcapPacket.getRawData());
                };
        try { handle.loop(-1,listener); }
        catch (Exception e) { e.printStackTrace(); }
    }
}