package SwitchAnalyzer.Network;

import SwitchAnalyzer.Kafka.Topics;
import SwitchAnalyzer.MainHandler_Node;
import org.pcap4j.core.PacketListener;
import org.pcap4j.core.PcapHandle;
import org.pcap4j.core.PcapPacket;
import org.pcap4j.packet.EthernetPacket;
import org.pcap4j.packet.IpV4Packet;
import org.pcap4j.packet.Packet;
import org.pcap4j.packet.UnknownPacket;
import org.pcap4j.util.MacAddress;

import java.util.Arrays;

public class FrameProcessing
{
    public static FrameResult processFrames(byte[] frameBytes)
    {
        FrameResult frameResult = new FrameResult();
        Packet.Builder payloadBuild = Builder.getBuilder(new PayloadBuilder(new String(frameBytes)),
                new UnknownPacket.Builder());
        EthernetPacket packet = null;
        try { packet = EthernetPacket.newPacket(frameBytes,0,frameBytes.length); }
        catch (Exception e) { e.printStackTrace(); }
        Packet.Builder builder =  packet.getBuilder();
        Packet.Builder builder1 =  builder.getPayloadBuilder();
        Packet.Builder builder2 = builder1.getPayloadBuilder();
        Packet.Builder builder3 = builder2.getPayloadBuilder();
        System.out.println(getType(builder.build().getHeader()));
        System.out.println(getType(builder1.build().getHeader()));
        System.out.println(getType(builder2.build().getHeader()));



        return frameResult;
    }

    public static String getType(Packet.Header header)
    {
        String headerString = header.toString();
        StringBuilder result = new StringBuilder("");
        int i = 0;
        while(headerString.charAt(i) != ']')
        {
            char c = headerString.charAt(i++);
            if (c == '[')
                continue;
            if (c == '(')
                break;
            result.append(c);
        }
        return result.toString();
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
