package SwitchAnalyzer.Network;

import SwitchAnalyzer.MainHandler_Node;
import SwitchAnalyzer.Sockets.PacketInfoGui;
import org.pcap4j.core.BpfProgram;
import org.pcap4j.core.PacketListener;
import org.pcap4j.core.PcapHandle;
import org.pcap4j.core.PcapPacket;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class PacketSniffer
{
    public static int recievedPacketCount;
    private static final ArrayList<PacketInfoGui> packetInfoGuis = new ArrayList<>();

    public PacketSniffer(){}

    public static void addToPacketInfoList(PacketInfoGui packetInfoGui) { packetInfoGuis.add(packetInfoGui); }

    public static void openThreads()
    {
        ArrayList<Thread> threads = new ArrayList<>();
        for (PacketInfoGui packetInfo : packetInfoGuis)
        {
            Thread t = new Thread (() -> startRead(packetInfo));
            threads.add(t);
            t.start();
        }
        for (Thread t : threads)
        {
            try{ t.join(); }
            catch (Exception ignored){}
        }
        packetInfoGuis.clear();
    }

    public static void startRead(PacketInfoGui packetInfoGui)
    {

        PcapHandle handle = PCAP.createHandle();
        try
        {
            handle.setFilter(PacketSniffer.getStringFromPacketInfo(packetInfoGui), BpfProgram.BpfCompileMode.OPTIMIZE);
            PacketListener listener =
                    new PacketListener() {
                        @Override
                        public void gotPacket(PcapPacket pcapPacket) {
                            System.out.println(Thread.currentThread().getId());
                            try {
                                Thread.sleep(1000);
                            } catch (InterruptedException e) {
                            }
                        }
                    };
            ExecutorService pool = Executors.newCachedThreadPool();
            handle.loop((int) packetInfoGui.numberOfPackets, listener, pool);
        }
        catch(Exception e){ System.out.println("Couldn't set Filter"); e.printStackTrace(); }
        try
        {
            System.out.println(recievedPacketCount +  " " +  handle.getStats().getNumPacketsReceived());
            handle.close();
        }
        catch (Exception ignored){}
    }

    public static String  getStringFromPacketInfo(PacketInfoGui packetInfoGui)
    {
        StringBuilder filter = new StringBuilder();
        filter.append("outbound and ");
        if (packetInfoGui.networkHeader.equals("ipv4"))
            filter.append("ip and ");
        filter.append(packetInfoGui.transportHeader);
        filter.append(" port 12345 and ");
        filter.append("ether dst ").append("2C:F0:5D:59:F9:7C");
        System.out.println(filter);
        return filter.toString();
    }

    public static void main(String[] args)
    {
        PCAP.initialize();
        byte [] bytes = new byte[1000];
        PacketInfoGui packetInfoGui = new PacketInfoGui("Ethernet", "ipv4", "udp", bytes.toString(), "None",
                1042,1000000,false);
        PacketSniffer.addToPacketInfoList(packetInfoGui);
        PacketSniffer.openThreads();
    }
}
