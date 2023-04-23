package SwitchAnalyzer.Database;

import org.apache.kafka.common.protocol.types.Field;
import org.pcap4j.packet.IpV4Packet;
import org.pcap4j.packet.Packet;

public class DBIpv4 extends DBNetworkHeader
{
    private int versionNo;
    /**
     * ipHeaderLength will be something like this => 5 (20 [bytes])
     */
    private String ipHeaderLength;
    /**
     * Tos will be something like this => [precedence: 0 (Routine)] [tos: 0 (Default)] [mbz: 0]
     */
    private String TOS;
    /**
     * This the length of the header and the payload of the ipv4 (transport header and data)
     */
    private int totalLengthInBytes;
    private int ID;
    private boolean reservedFlag =false;
    private boolean dontFragmentFlag;
    private boolean moreFragmentFlag;
    private int fragmentOffset;
    private int timeToLive;
    /**
     * upperLayerProtocol will be something like this => 17 (UDP)
     */
    private String upperLayerProtocol;
    /**
     * headerChecksum is a string the has the hex value of headerChecksum
     */
    private String headerChecksum;
    /**
     * sourceIpAddress will be something like this => 192.168.1.1
     */
    private String sourceIpAddress;
    /**
     * destinationIpAddress will be something like this => 192.168.1.1
     */
    private String destinationIpAddress;

    public DBIpv4(String networkHeaderName,String ipv4HeaderBytes)
    {
        super(networkHeaderName,ipv4HeaderBytes);
    }

    public int getVersionNo() {
        return versionNo;
    }

    public void setVersionNo(int versionNo) {
        this.versionNo = versionNo;
    }

    public String getIpHeaderLength() {
        return ipHeaderLength;
    }
    /**
     * Input : Ip header length in 32 bit words
     * Output : void
     * Description :
     * This function makes a string from knowing the length passed
     * The string is => Ip header length in 32 bit words (4*Ip header length in 32 bit words [bytes])
     * Now we have the string that tells us the ip header length in 32 bit words and in bytes
     */
    public void setIpHeaderLength(int ipHeaderLength)
    {
        this.ipHeaderLength = ""+ipHeaderLength+" ("+4*ipHeaderLength+" [bytes])";
    }
    public String getTOS() {
        return TOS;
    }

    public void setTOS(String TOS) {
        this.TOS = TOS;
    }

    public int getTotalLengthInBytes() {
        return totalLengthInBytes;
    }

    public void setTotalLengthInBytes(int totalLengthInBytes) {
        this.totalLengthInBytes = totalLengthInBytes;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public boolean isReservedFlag() {
        return reservedFlag;
    }

    public void setReservedFlag(boolean reservedFlag) {
        this.reservedFlag = reservedFlag;
    }

    public boolean isDontFragmentFlag() {
        return dontFragmentFlag;
    }

    public void setDontFragmentFlag(boolean dontFragmentFlag) {
        this.dontFragmentFlag = dontFragmentFlag;
    }

    public boolean isMoreFragmentFlag() {
        return moreFragmentFlag;
    }

    public void setMoreFragmentFlag(boolean moreFragmentFlag) {
        this.moreFragmentFlag = moreFragmentFlag;
    }

    public int getFragmentOffset() {
        return fragmentOffset;
    }

    public void setFragmentOffset(int fragmentOffset) {
        this.fragmentOffset = fragmentOffset;
    }

    public int getTimeToLive() {
        return timeToLive;
    }

    public void setTimeToLive(int timeToLive) {
        this.timeToLive = timeToLive;
    }

    public String getUpperLayerProtocol() {
        return upperLayerProtocol;
    }

    public void setUpperLayerProtocol(String upperLayerProtocol) {
        this.upperLayerProtocol = upperLayerProtocol;
    }

    public String getHeaderChecksum() {
        return headerChecksum;
    }

    public void setHeaderChecksum(String headerChecksum) {
        this.headerChecksum = "0x"+headerChecksum;
    }

    public String getSourceIpAddress() {
        return sourceIpAddress;
    }

    public void setSourceIpAddress(String sourceIpAddress) {
        this.sourceIpAddress = sourceIpAddress;
    }

    public String getDestinationIpAddress() {
        return destinationIpAddress;
    }

    public void setDestinationIpAddress(String destinationIpAddress) {
        this.destinationIpAddress = destinationIpAddress;
    }
    /**
     * Input : Packet received
     * Output : void
     * Description :
     * This function takes the packet and convert it to IPv4 packet
     * and then sets every part of the header of the ipv4 in the DBIpv4 object
     */

    public void getNetworkHeaderDetails(Packet packet)
    {
        IpV4Packet ipV4Packet = packet.get(IpV4Packet.class);
        IpV4Packet.IpV4Header ipv4Header = ipV4Packet.getHeader();
        System.out.println(ipv4Header.toString());
//        //The payload of the ipv4 which is the transport header and the data
//        String payloadOfIpv4 = ipV4Packet.getPayload().toString();
//        System.out.println(payloadOfIpv4);
        setVersionNo(ipv4Header.getVersion().value());
        setIpHeaderLength(ipv4Header.getIhl());
        setTOS(ipv4Header.getTos().toString());
        setTotalLengthInBytes(ipv4Header.getTotalLengthAsInt());
        setID(ipv4Header.getIdentificationAsInt());
        setReservedFlag(ipv4Header.getReservedFlag());
        setDontFragmentFlag(ipv4Header.getDontFragmentFlag());
        setMoreFragmentFlag(ipv4Header.getMoreFragmentFlag());
        setFragmentOffset(ipv4Header.getFragmentOffset());
        setTimeToLive(ipv4Header.getTtlAsInt());
        setUpperLayerProtocol(ipv4Header.getProtocol().toString());
        setHeaderChecksum(Integer.toHexString(ipv4Header.getHeaderChecksum() & 0xffff));
        //System.out.println("************************"+ipv4Header.getHeaderChecksum());
        setSourceIpAddress(ipv4Header.getSrcAddr().getHostAddress());
        setDestinationIpAddress(ipv4Header.getDstAddr().getHostAddress());
    }

    @Override
    public String toString() {
        return super.toString()+"\nDBIpv4{" +
                "\nversionNo=" + versionNo +
                "\n, ipHeaderLength=" + ipHeaderLength +
                "\n, TOS='" + TOS + '\'' +
                "\n, totalLengthInBytes=" + totalLengthInBytes +
                "\n, ID=" + ID +
                "\n, reservedFlag=" + reservedFlag +
                "\n, dontFragmentFlag=" + dontFragmentFlag +
                "\n, moreFragmentFlag=" + moreFragmentFlag +
                "\n, fragmentOffset=" + fragmentOffset +
                "\n, timeToLive=" + timeToLive +
                "\n, upperLayerProtocol='" + upperLayerProtocol + '\'' +
                "\n, headerChecksum='" + headerChecksum + '\'' +
                "\n, sourceIpAddress='" + sourceIpAddress + '\'' +
                "\n, destinationIpAddress='" + destinationIpAddress + '\'' +
                "\n, networkHeaderName='" + getNetworkHeaderName() + '\'' +
                "\n, networkHeaderBytes='" + getNetworkFullHeader() + '\'' +
                "\n}\n";
    }
}
