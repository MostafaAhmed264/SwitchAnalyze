package SwitchAnalyzer.Network;

import SwitchAnalyzer.Machines.MachineNode;
import SwitchAnalyzer.Machines.MasterOfHPC;
import SwitchAnalyzer.UtilityExecution.UtilityExecutor;
import SwitchAnalyzer.miscellaneous.GlobalVariable;
import jnr.ffi.annotations.Synchronized;

import java.util.ArrayList;


public class SendThreadsHandler
{
    private static final ArrayList<PacketInfo> packetInfos = new ArrayList<>();
    public static ArrayList<Thread> threads = new ArrayList<>();

    public static void addToPacketInfoList(PacketInfo info) { packetInfos.add(info); }

    public static void sendToSelectedPort(int toPort, int rate, long duration)
    {
        ArrayList<Thread> threads = new ArrayList<>();
        MasterOfHPC selectedHPC = PortSelector.selectForPort(toPort);
        for (MachineNode node : selectedHPC.childNodes)
        {
            Thread t = new Thread(()-> openThreads(toPort , node, rate, duration));
            threads.add(t);
            t.start();
        }
        for (Thread thread : threads )
        {
            try { thread.join(); }
            catch (Exception ignored){}
        }
        clearPacketInfos();
        clearThreads();
        UtilityExecutor.clearUtils();

    }

    public static void openThreads(int toPort , MachineNode node, int rate, long duration)
    {
        for (PacketInfo packetInfo : packetInfos)
        {
            packetInfo.setHeaderValues(node);
            NormalSender sender = new NormalSender(packetInfo.build() ,
                    packetInfo.numberOfPackets /PortSelector.selectForPort(toPort).childNodes.size(), rate, duration);
            Thread t = new Thread(sender);
            threads.add(t);
            t.start();
        }
        for (Thread thread : threads)
        {
            try { thread.join(); }
            catch (Exception ignored){}
        }
    }

    public static void stopThreads()
    {
        GlobalVariable.stopRunSignal = true;
    }

    public static void resumeThreads()
    {
        GlobalVariable.stopRunSignal = false;
        try {  synchronized (NormalSender.monitor) { NormalSender.monitor.notifyAll(); } }
        catch (Exception ignored){}
    }

    public static void clearThreads() { threads.clear(); }
    public static void clearPacketInfos() { packetInfos.clear(); }
}
