package SwitchAnalyzer.Database;

import org.pcap4j.packet.IpV6Packet;
import org.pcap4j.packet.Packet;

public class DBIpv6 extends DBNetworkHeader
{
    private int versionNo;
    /**
     * trafficClass will have the hex value of the trafficClass
     */
    private int trafficClass;
    /**
     * flowLabel will have the hex value of the flowLabel
     */
    private int flowLabel;
    private int payloadLength;
    private int nextHeader;
    private int hopLimit;
    private String srcMacAddress;
    private String dstMacAddress;
    public DBIpv6(String networkHeaderName,String ipv6HeaderBytes)
    {
        super(networkHeaderName,ipv6HeaderBytes);
    }

    public int getVersionNo() {
        return versionNo;
    }

    public void setVersionNo(int versionNo) {
        this.versionNo = versionNo;
    }

    public int getTrafficClass() {
        return trafficClass;
    }

    public void setTrafficClass(int trafficClass) {
        this.trafficClass = trafficClass;
    }

    public int getFlowLabel() {
        return flowLabel;
    }

    public void setFlowLabel(int flowLabel) {
        this.flowLabel = flowLabel;
    }

    public int getPayloadLength() {
        return payloadLength;
    }

    public void setPayloadLength(int payloadLength) {
        this.payloadLength = payloadLength;
    }

    public int getNextHeader() {
        return nextHeader;
    }

    public void setNextHeader(int nextHeader) {
        this.nextHeader = nextHeader;
    }

    public int getHopLimit() {
        return hopLimit;
    }

    public void setHopLimit(int hopLimit) {
        this.hopLimit = hopLimit;
    }

    public String getSrcMacAddress() {
        return srcMacAddress;
    }

    public void setSrcMacAddress(String srcMacAddress) {
        this.srcMacAddress = srcMacAddress;
    }

    public String getDstMacAddress() {
        return dstMacAddress;
    }

    public void setDstMacAddress(String dstMacAddress) {
        this.dstMacAddress = dstMacAddress;
    }
    /**
     * Input : Packet received
     * Output : void
     * Description :
     * This function takes the packet and convert it to IPv6 packet
     * and then sets every part of the header of the ipv6 in the DBIpv6 object
     */

    public void getNetworkHeaderDetails(Packet packet)
    {
        IpV6Packet ipV6Packet = packet.get(IpV6Packet.class);
        IpV6Packet.IpV6Header ipv6Header = ipV6Packet.getHeader();
        //System.out.println(ipv6Header.toString());
        //The payload of the ipv6 which is the transport header and the data
//        String payloadOfIpv6 = ipV6Packet.getPayload().toString();
//        System.out.println(payloadOfIpv6);
        setVersionNo(ipv6Header.getVersion().value());
        setTrafficClass(ipv6Header.getTrafficClass().value());
        setFlowLabel(ipv6Header.getFlowLabel().value());
        setPayloadLength(ipv6Header.getPayloadLengthAsInt());
        setNextHeader(ipv6Header.getNextHeader().value());
        setHopLimit(ipv6Header.getHopLimitAsInt());
        setSrcMacAddress(ipv6Header.getSrcAddr().getHostAddress());
        setDstMacAddress(ipv6Header.getDstAddr().getHostAddress());
    }

    @Override
    public String toString() {
        return super.toString() + "\nDBIpv6{" +
                "\nversionNo=" + versionNo +
                "\n, trafficClass=" + trafficClass +
                "\n, flowLabel=" + flowLabel +
                "\n, payloadLength=" + payloadLength +
                "\n, nextHeader=" + nextHeader +
                "\n, hopLimit=" + hopLimit +
                "\n, srcMacAddress='" + srcMacAddress + '\'' +
                "\n, dstMacAddress='" + dstMacAddress + '\'' +
                "\n, networkHeaderName='" + getNetworkHeaderName() + '\'' +
                "\n, networkHeaderBytes='" + getNetworkFullHeader() + '\'' +
                "\n}\n";
    }
}
