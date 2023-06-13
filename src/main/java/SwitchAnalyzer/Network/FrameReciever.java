package SwitchAnalyzer.Network;

import SwitchAnalyzer.Kafka.Topics;
import SwitchAnalyzer.MainHandler_Node;
import org.pcap4j.core.BpfProgram;
import org.pcap4j.core.PacketListener;
import org.pcap4j.core.PcapHandle;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class FrameReciever
{
    private static final PcapHandle handle_IN = PCAP.createHandle();
    private static final PcapHandle handle2_OUT = PCAP.createHandle();
    private static final PcapHandle plossHandle = PCAP.createHandle();
    private static final short plossPort = 9974;
    static int count = 0;
    public static void startRec()
    {


        try
        {
            handle_IN.setDirection(PcapHandle.PcapDirection.IN);
            handle2_OUT.setDirection(PcapHandle.PcapDirection.OUT);

            PacketListener listener = pcapPacket ->
            {
                count ++;
                if (count % 10 == 0)
                    MainHandler_Node.packetProducer.send(Topics.FramesFromHPC_IN, pcapPacket.getRawData());
            };
            
            PacketListener listener1 =  pcapPacket ->
            {
                count ++;
                if (count % 10 == 0)
                    MainHandler_Node.packetProducer.send(Topics.FramesFromHPC_OUT, pcapPacket.getRawData());
            };

            ExecutorService pool = Executors.newCachedThreadPool();
            ExecutorService pool2 = Executors.newCachedThreadPool();

            Thread t = new Thread( () -> 
            {
                try { handle_IN.loop(-1,listener,pool); }
                catch (Exception e) {}
            });

            Thread t2 = new Thread( () ->
            {
                try { handle2_OUT.loop(-1,listener1,pool2); }
                catch (Exception e) {}
            });
            
            t.start(); 
            t2.start();
        }
        
        
        catch(Exception e){ System.out.println("Couldn't set Filter"); e.printStackTrace(); }
        try { handle_IN.close(); }
        catch (Exception ignored){}
    }

    public static void endRec()
    { 
        try 
        { 
            handle_IN.breakLoop();
            handle_IN.close();
            handle2_OUT.breakLoop();
            handle2_OUT.close();
        } catch (Exception e) {}
    }

    public static void main(String[] args)
    {
        while(true)
            startRec();
    }
}
