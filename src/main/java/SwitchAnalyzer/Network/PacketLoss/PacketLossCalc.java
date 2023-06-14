package SwitchAnalyzer.Network.PacketLoss;

import SwitchAnalyzer.MainHandler_Master;
import SwitchAnalyzer.MainHandler_Node;
import SwitchAnalyzer.NamingConventions;
import SwitchAnalyzer.Network.*;
import SwitchAnalyzer.Sockets.PacketInfoGui;
import SwitchAnalyzer.UtilityExecution.UtilityExecutor;
import SwitchAnalyzer.miscellaneous.GlobalVariable;
import org.pcap4j.core.*;
import org.pcap4j.packet.*;
import org.pcap4j.packet.namednumber.EtherType;
import org.pcap4j.packet.namednumber.IpNumber;
import org.pcap4j.packet.namednumber.IpVersion;
import org.pcap4j.packet.namednumber.UdpPort;
import org.pcap4j.util.MacAddress;

import java.net.Inet4Address;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static SwitchAnalyzer.Network.PCAP.nif;

public class PacketLossCalc
{
    public static final short plossPort = 9778;
    public static final short echoPlossPort = 9999;
    public static Packet plossPacket ;
    public  PcapHandle sendHandle;
    public  PcapHandle echoHandle;
    public PcapHandle genEchoHandle;
    private static final int SNAPLEN = 65536;
    public static final int count = 5;
    private static final int READ_TIMEOUT = 10;
    public static int recievedPacketCount = 0;
    private static final ExecutorService pool = Executors.newSingleThreadExecutor();
    ArrayList<Long> sendTimes = new ArrayList<Long>();
    ArrayList<Long> receiveTimes = new ArrayList<Long>();

    public  void init()
    {

        byte[] echoData = new byte[1000 - 28];
        for (int i = 0; i < echoData.length; i++) {echoData[i] = (byte) i;}

        try
        {
            sendHandle = nif.openLive(SNAPLEN, PcapNetworkInterface.PromiscuousMode.PROMISCUOUS, READ_TIMEOUT);
            UdpPacket.Builder udpBuilder = new UdpPacket.Builder();
            udpBuilder
                    .payloadBuilder(new UnknownPacket.Builder().rawData(echoData))
                    .dstPort(UdpPort.getInstance(plossPort))
                    .srcPort(UdpPort.getInstance((short) 8855))
                    .correctLengthAtBuild(true);
            int randomPort = PortSelector.selectRandomPort2();

            System.out.println("the selected port is : " + randomPort);

            IpV4Packet.Builder ipBuilder = new IpV4Packet.Builder();
            ipBuilder
                    .tos((IpV4Packet.IpV4Tos) () -> (byte) 0)
                    .identification((short) new Random().nextInt())
                    .ttl((byte) 100)
                    .srcAddr(MainHandler_Node.node.nodeIp)
                    .dstAddr(GlobalVariable.portHpcMap.get(randomPort).machineNode.nodeIp)
                    .payloadBuilder(udpBuilder)
                    .correctChecksumAtBuild(true)
                    .correctLengthAtBuild(true);
            ipBuilder.protocol(IpNumber.UDP);
            ipBuilder.version(IpVersion.IPV4);

            EthernetPacket.Builder etherBuilder = new EthernetPacket.Builder();
            etherBuilder
                    .dstAddr(GlobalVariable.portHpcMap.get(randomPort).machineNode.nodeMacAddress)
                    .srcAddr(MainHandler_Node.node.nodeMacAddress)
                    .type(EtherType.IPV4)
                    .paddingAtBuild(true)
                    .payloadBuilder(ipBuilder);
            plossPacket = etherBuilder.build();
        } catch (PcapNativeException e) {
            e.printStackTrace();
        }
    }


    public  void calculatePacketLoss()
    {
        init();
        startEchoListner();
        for (int i = 0 ; i < count ; ++i)
        {
            try
            {
                sendHandle.sendPacket(plossPacket);
                sendTimes.add(System.nanoTime());
            }
            catch (Exception e) {}
            try {Thread.sleep(100);}
            catch (InterruptedException e) {break;}
            try {Thread.sleep(244);}
            catch (InterruptedException e) {break;}
        }
    }
    public  void startEchoListner()
    {
        //listen on other port
        try {
            echoHandle = nif.openLive(SNAPLEN, PcapNetworkInterface.PromiscuousMode.PROMISCUOUS, READ_TIMEOUT);
            echoHandle.setDirection(PcapHandle.PcapDirection.IN);
            try {
                echoHandle.setFilter("udp dst port "+echoPlossPort ,  BpfProgram.BpfCompileMode.OPTIMIZE);
                PacketListener listener =
                        new PacketListener() {
                            @Override
                            public void gotPacket(PcapPacket pcapPacket)
                            {
                                recievedPacketCount++;
                                receiveTimes.add(System.nanoTime());
                            }
                        };
                Task t = new Task(echoHandle, listener);
                //pool.execute(t);
                Thread t1 = new Thread(t);
                t1.start();
            } catch (NotOpenException e) {
                throw new RuntimeException(e);
            }
        } catch (PcapNativeException e) {
            throw new RuntimeException(e);
        } catch (NotOpenException e) {
            throw new RuntimeException(e);
        }
    }
    public void generateEcho()
    {
        try {
            genEchoHandle = nif.openLive(SNAPLEN, PcapNetworkInterface.PromiscuousMode.PROMISCUOUS, READ_TIMEOUT);
            genEchoHandle.setDirection(PcapHandle.PcapDirection.IN);
            genEchoHandle.setFilter("udp dst port "+ plossPort ,  BpfProgram.BpfCompileMode.OPTIMIZE);
            PacketListener listener =
                    new PacketListener() {
                        @Override
                        public void gotPacket(PcapPacket pcapPacket)
                        {
                            Packet p;
                            Packet.Builder builder = pcapPacket.getBuilder();
                            EthernetPacket.Builder ethBuilder = builder.get(EthernetPacket.Builder.class);
                            MacAddress srcAddr = pcapPacket.get(EthernetPacket.class).getHeader().getSrcAddr();
                            ethBuilder.dstAddr(srcAddr);
                            ethBuilder.srcAddr(MainHandler_Master.master.machineNode.nodeMacAddress);
                            IpV4Packet.Builder ipbuilder = builder.get(IpV4Packet.Builder.class);
                            Inet4Address srcIpAddr = pcapPacket.get(IpV4Packet.class).getHeader().getSrcAddr();
                            ipbuilder.dstAddr(srcIpAddr);
                            ipbuilder.srcAddr(MainHandler_Master.master.machineNode.nodeIp);
                            UDPModifier.modifiyDstPort(builder,echoPlossPort);
                            p = builder.build();
                            try {
                                genEchoHandle.sendPacket(p);
                            } catch (PcapNativeException e) {
                                throw new RuntimeException(e);
                            } catch (NotOpenException e) {
                                throw new RuntimeException(e);
                            }
                        }

                    };
            Task t = new Task(genEchoHandle, listener);
            Thread t1 = new Thread(t);
            t1.start();
            //pool.execute(t);
        } catch (PcapNativeException | NotOpenException e) {
            throw new RuntimeException(e);
        }

    }
    public void stop_echoing()
    {
        try { genEchoHandle.breakLoop(); }
        catch (NotOpenException e) { throw new RuntimeException(e); }
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

    public static float startPacketLossTest()
    {
        PacketLossCalc packetLossCalculate = new PacketLossCalc();
        packetLossCalculate.calculatePacketLoss();
        float pl = (((float)count - packetLossCalculate.recievedPacketCount)/count) * 100;
        if (pl < 0)
            return 0;
        UtilityExecutor.result.put(NamingConventions.latency, String.valueOf(packetLossCalculate.calculateLatency() * Math.pow(10, -6)));
        return pl ;
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
}
