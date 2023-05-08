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
    public static int counter = 0;

    public static void startRec()
    {
        try
        {
            PacketListener listener = pcapPacket -> {
                counter++;
                if (counter % 10 == 0) {
                    MainHandler_Node.packetProducer.send(Topics.FramesFromHPC, pcapPacket.getRawData());
                }
            };
            ExecutorService pool = Executors.newCachedThreadPool();
            handle.loop(-1, listener, pool);
        }
        catch(Exception e){ System.out.println("Couldn't set Filter"); e.printStackTrace(); }
        try { handle.close(); }
        catch (Exception ignored){}
    }

    public static void endRec() { try { handle.breakLoop(); handle.close(); } catch (Exception e) {}}

    public static void main(String[] args)
    {
        while(true)
            startRec();
    }
}
