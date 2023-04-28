package SwitchAnalyzer.Network;

import SwitchAnalyzer.Kafka.Topics;
import SwitchAnalyzer.MainHandler_Node;
import SwitchAnalyzer.miscellaneous.GlobalVariable;
import org.pcap4j.core.PacketListener;
import org.pcap4j.core.PcapHandle;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class FrameReciever
{
    private static final PcapHandle handle = PCAP.createHandle();

    public static void startRec()
    {
        try
        {
            PacketListener listener = pcapPacket ->
                MainHandler_Node.packetProducer.send(Topics.FramesFromHPC, pcapPacket.getRawData());
            ExecutorService pool = Executors.newCachedThreadPool();
            if (!GlobalVariable.stopRecieving)
                handle.loop(-1, listener, pool);
            else
                handle.breakLoop();
        }
        catch(Exception e){ System.out.println("Couldn't set Filter"); e.printStackTrace(); }
        try { handle.close(); }
        catch (Exception ignored){}
    }
}
