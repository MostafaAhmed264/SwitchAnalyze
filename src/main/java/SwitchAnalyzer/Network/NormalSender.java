package SwitchAnalyzer.Network;
import com.google.gson.internal.bind.util.ISO8601Utils;
import org.pcap4j.core.NotOpenException;
import org.pcap4j.core.PcapNativeException;
import org.pcap4j.packet.Packet;

public class NormalSender extends Sender implements Runnable
{
    public NormalSender(Packet packet , long numPackets, int sendingRate,long duration)
    {
        super(packet,numPackets,duration,sendingRate);
    }

    @Override
    public void send()
    {
        try
        {
            if(duration > 0) durationSending();
            else rateSending();
        }
        catch (Exception e)
        {
            System.out.println("Error in sending");
            e.printStackTrace();
        }
    }
    @Override
    public void run()
    {
        send();
    }

    public void durationSending()
    {
        long startTime = System.currentTimeMillis();
        while (System.currentTimeMillis() - startTime <= duration)
        {
            try { PCAP.handle.sendPacket(packet); }
            catch (PcapNativeException | NotOpenException e) { throw new RuntimeException(e); }
        }
    }
    public void rateSending()
    {
        double differenceTime=1e9/(double)sendingRate;//in milli
        for (int i = 0; i < numPackets; i++)
        {
            try
            {
                long startTime=System.nanoTime();
                PCAP.handle.sendPacket(packet);
                long endTime =System.nanoTime();
                if(sendingRate > 0)
                {
                    long currTime = System.nanoTime();
                    while (System.nanoTime() <= currTime+ (differenceTime -(endTime - startTime)));
                }

            }
            catch (PcapNativeException | NotOpenException e) { throw new RuntimeException(e); }
        }
    }
}

