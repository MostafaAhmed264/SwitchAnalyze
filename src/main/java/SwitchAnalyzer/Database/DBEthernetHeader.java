package SwitchAnalyzer.Database;

import org.pcap4j.packet.EthernetPacket;
import org.pcap4j.packet.Packet;

public class DBEthernetHeader
{
    private String srcAddress;
    private String dstAddress;
    private String type;

    public DBEthernetHeader(){}
    public DBEthernetHeader(String srcAddress, String dstAddress, String type) {
        srcAddress = srcAddress;
        dstAddress = dstAddress;
        this.type = type;
    }

    public String getSrcAddress() {
        return srcAddress;
    }

    public void setSrcAddress(String srcAddress) {
        srcAddress = srcAddress;
    }

    public String getDstAddress() {
        return dstAddress;
    }

    public void setDstAddress(String dstAddress) {
        dstAddress = dstAddress;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    /**
     * Input : packet received
     * Output : String
     * Description :
     * This function will retrieve the destination address of the packet received "not used yet"
     */
    public void getDstMac(Packet packet)
    {
        this.dstAddress = packet.get(EthernetPacket.class).getHeader().getDstAddr().toString();
    }
    /**
     * Input : packet received
     * Output : String
     * Description :
     * This function will retrieve the source address of the packet received "not used yet"
     */
    public void getSrcMac(Packet packet)
    {
        this.srcAddress = packet.get(EthernetPacket.class).getHeader().getSrcAddr().toString();
    }
}
