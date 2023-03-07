package SwitchAnalyzer.Network;
import org.pcap4j.core.PcapHandle;
import org.pcap4j.core.PcapNetworkInterface;
import org.pcap4j.core.Pcaps;

import java.util.List;

public class PCAP {

    public static PcapNetworkInterface nif;

    public static PcapHandle handle;

    public static void initialize()
    {
        try
        {
            List<PcapNetworkInterface> allDevs = Pcaps.findAllDevs();
            nif = allDevs.get(0);
        }
        catch (Exception ex)
        {
            System.out.println("Problem in setting pcap network device");
        }
        int snapLen = 2048;
        PcapNetworkInterface.PromiscuousMode mode = PcapNetworkInterface.PromiscuousMode.NONPROMISCUOUS;
        int timeout = 100;
        try
        {
            handle = nif.openLive(snapLen, mode, timeout);
        }
        catch (Exception ex)
        {
            System.out.println("Problem in opening session");
        }
    }
    static void close_session()
    {
        handle.close();
    }
}
