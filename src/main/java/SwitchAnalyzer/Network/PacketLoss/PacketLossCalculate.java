package SwitchAnalyzer.Network.PacketLoss;

import SwitchAnalyzer.Machines.MasterOfHPC;
import SwitchAnalyzer.MainHandler_Node;
import SwitchAnalyzer.NamingConventions;
import SwitchAnalyzer.Network.*;
import SwitchAnalyzer.UtilityExecution.UtilityExecutor;
import SwitchAnalyzer.miscellaneous.GlobalVariable;
import org.pcap4j.core.*;
import org.pcap4j.packet.*;
import org.pcap4j.packet.namednumber.*;
import org.pcap4j.util.MacAddress;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static SwitchAnalyzer.Network.MACAddr.srcMacAddr;
import static SwitchAnalyzer.Network.PCAP.nif;

public class PacketLossCalculate {

    public static final int COUNT = 5;
    private static final int READ_TIMEOUT = 10; //IN MS
    private static final int SNAPLEN = 65536;
    public  String echoData = "asdfdsffdsfsdsfdafsfdsfds";
    public int recievedPacketCount = 0;
    private PcapHandle handle;
    private PcapHandle sendHandle;
    private final ExecutorService pool = Executors.newSingleThreadExecutor();
    ArrayList<Long> sendTimes = new ArrayList<Long>();
    ArrayList<Long> receiveTimes = new ArrayList<Long>();


    private void init()
    {
        try
        {
            handle = nif.openLive(SNAPLEN, PcapNetworkInterface.PromiscuousMode.PROMISCUOUS, READ_TIMEOUT);
            sendHandle = nif.openLive(SNAPLEN, PcapNetworkInterface.PromiscuousMode.PROMISCUOUS, READ_TIMEOUT);
        }
        catch (Exception ignored){}
    }
    public void startEchoLisitner()
    {
        try
        {
            handle.setFilter("icmp and ether dst " + Pcaps.toBpfString(MainHandler_Node.node.nodeMacAddress), BpfProgram.BpfCompileMode.OPTIMIZE);
            PacketListener listener =
                    new PacketListener() {
                        @Override
                        public void gotPacket(PcapPacket pcapPacket) {
                            recievedPacketCount++;
                            receiveTimes.add(System.nanoTime());
                        }
                    };
            Task t = new Task(handle, listener);
            pool.execute(t);
        }
        catch (Exception ignored){}
    }

    public void claculatePacketLoss()
    {
        init();
        startEchoLisitner();
        try
        {

            byte[] echoData = new byte[1000 - 28];
            for (int i = 0; i < echoData.length; i++) {echoData[i] = (byte) i;}

            IcmpV4EchoPacket.Builder echoBuilder = new IcmpV4EchoPacket.Builder();
            echoBuilder
                    .identifier((short) 1)
                    .payloadBuilder(new UnknownPacket.Builder().rawData(echoData));

            IcmpV4CommonPacket.Builder icmpV4CommonBuilder = new IcmpV4CommonPacket.Builder();
            icmpV4CommonBuilder
                    .type(IcmpV4Type.ECHO)
                    .code(IcmpV4Code.NO_CODE)
                    .payloadBuilder(echoBuilder)
                    .correctChecksumAtBuild(true);

            IpV4Packet.Builder ipV4Builder = new IpV4Packet.Builder();
            ipV4Builder
                    .version(IpVersion.IPV4)
                    .tos(IpV4Rfc791Tos.newInstance((byte) 0))
                    .ttl((byte) 100)
                    .protocol(IpNumber.ICMPV4)
                    .srcAddr(MainHandler_Node.node.nodeIp)
                    .dstAddr(GlobalVariable.portHpcMap.get(2).HPCIp)
                    .payloadBuilder(icmpV4CommonBuilder)
                    .correctChecksumAtBuild(true)
                    .correctLengthAtBuild(true);

            EthernetPacket.Builder etherBuilder = new EthernetPacket.Builder();
            etherBuilder
                    .dstAddr(GlobalVariable.portHpcMap.get(2).HPCMacAddr)
                    .srcAddr(MainHandler_Node.node.nodeMacAddress)
                    .type(EtherType.IPV4)
                    .paddingAtBuild(true);
            for (int i = 0; i < COUNT; i++) {
                (echoBuilder).sequenceNumber((short) i);
                (ipV4Builder).identification((short) i);
                (etherBuilder).payloadBuilder(ipV4Builder);
                Packet p = etherBuilder.build();
                sendTimes.add(System.nanoTime());
                sendHandle.sendPacket(p);
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    System.out.println(";-;");
                    break;
                }
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    System.out.println(";-;");
                    break;
                }
            }

        }
        catch(Exception ignored){}
        finally
        {
            if (handle != null && handle.isOpen())
            {
                try { handle.breakLoop(); }
                catch (NotOpenException ignored) {}
                try { Thread.sleep(100); }
                catch (InterruptedException ignored) {}
                handle.close();
            }
            if (sendHandle != null && sendHandle.isOpen()) { sendHandle.close(); }
            if (!pool.isShutdown()) { pool.shutdown(); }
        }
    }
    public long calculateLatency()
    {
        long max = -1;
        for (int i = 0 ; i < receiveTimes.size() ;++i)
        {
            long diff = receiveTimes.get(i) - sendTimes.get(i);
            if (diff > max)
                max = diff;
        }
        return max/2;
    }


    public static float startPacketLossTest()
    {
        PacketLossCalculate packetLossCalculate = new PacketLossCalculate();
        packetLossCalculate.claculatePacketLoss();
        float pl = (((float)COUNT - packetLossCalculate.recievedPacketCount)/COUNT) * 100;
        if (pl < 0)
            return 0;
        UtilityExecutor.result.put(NamingConventions.latency, String.valueOf(packetLossCalculate.calculateLatency()));
        return pl ;
    }

    private static class Task implements Runnable
    {
        private final PcapHandle handle;
        private final PacketListener listener;

        public Task(PcapHandle handle, PacketListener listener)
        {
            this.handle = handle;
            this.listener = listener;
        }
        @Override
        public void run()
        {
            try { handle.loop(-1, listener); }
            catch (PcapNativeException | NotOpenException e) { e.printStackTrace(); }
            catch (InterruptedException ignored) {}
        }
    }
}
