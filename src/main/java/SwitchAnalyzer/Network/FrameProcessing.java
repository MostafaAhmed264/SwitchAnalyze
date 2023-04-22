package SwitchAnalyzer.Network;

import org.pcap4j.core.PcapPacket;
import org.pcap4j.packet.EthernetPacket;
import org.pcap4j.packet.Packet;
import org.pcap4j.packet.UnknownPacket;

import java.util.Arrays;

public class FrameProcessing
{
    public static FrameResult processFrames(byte[] frameBytes)
    {
        FrameResult frameResult = new FrameResult();
        Packet.Builder payloadBuild = Builder.getBuilder(new PayloadBuilder(new String(frameBytes)),
                new UnknownPacket.Builder());
        Packet packet = payloadBuild.build();
        System.out.println(packet);
        return frameResult;
    }

    public static void main(String[] args)
    {

    }
}
