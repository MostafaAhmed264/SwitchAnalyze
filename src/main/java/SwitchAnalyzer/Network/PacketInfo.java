package SwitchAnalyzer.Network;

import SwitchAnalyzer.Machines.MachineNode;
import SwitchAnalyzer.MainHandler_Node;
import SwitchAnalyzer.Network.ErrorDetection.ErrorDetectingAlgorithms;
import org.pcap4j.packet.Packet;
import org.pcap4j.packet.UnknownPacket;

public class PacketInfo
{
    public DataLinkHeader dataLinkHeader;
    public NetworkHeader networkHeader;
    public TransportHeader transportHeader;
    public PayloadBuilder payloadBuilder;
    public ErrorDetectingAlgorithms errorDetectingAlgorithm;
    public long packetSize;
    public long numberOfPackets;

    public PacketInfo(){}

    public PacketInfo(PayloadBuilder payloadBuilder, TransportHeader transportHeader, NetworkHeader networkHeader,DataLinkHeader dataLinkHeader,ErrorDetectingAlgorithms errorDetectingAlgorithm)
    {
        this.payloadBuilder = payloadBuilder;
        this.transportHeader = transportHeader;
        this.networkHeader = networkHeader;
        this.dataLinkHeader = dataLinkHeader;
        this.errorDetectingAlgorithm=errorDetectingAlgorithm;
    }

    public Packet build()
    {
        Packet.Builder payloadBuild = Builder.getBuilder(this.payloadBuilder, new UnknownPacket.Builder());
        Packet.Builder udpBuild = Builder.getBuilder(this.transportHeader, payloadBuild);
        Packet.Builder networkBuild = Builder.getBuilder(this.networkHeader, udpBuild);
        Packet.Builder etherBuild = Builder.getBuilder(this.dataLinkHeader, networkBuild);
        Packet.Builder CRCBuild = Builder.getBuilder(this.errorDetectingAlgorithm,etherBuild);
        return CRCBuild.build();
    }

    public void setHeaderValues(MachineNode dstNode)
    {
        setAddresses(dstNode);
        setPortsDefValue();
    }

    private void setAddresses(MachineNode dstNode)
    {
        this.networkHeader.srcIPADDR = MainHandler_Node.node.nodeIp;
        this.dataLinkHeader.srcMac = MainHandler_Node.node.nodeMacAddress;
        this.networkHeader.dstIPPADDR = dstNode.nodeIp;
        this.dataLinkHeader.dstMac = dstNode.nodeMacAddress;
    }

    private void setPortsDefValue()
    {
        this.transportHeader.srcPort = (short)12345;
        this.transportHeader.dstPort = (short)54321;
    }
}
