package SwitchAnalyzer.Database;

import org.pcap4j.packet.Packet;
import org.pcap4j.packet.UdpPacket;

public class DBUdp extends DBTransportHeader
{
    int sourcePortNo;
    int destinationPortNo;
    /**
     * This is the length of the header and the payload of the udp (data)
     */
    int lengthInBytes;
    //This will have the hex value of the checksum
    String checksum;

    public int getSourcePortNo() {
        return sourcePortNo;
    }
    /**
     * Input : source port number
     * Output : void
     * Description :
     * This function takes the source port number that is passed from the Udp packet
     * and reverse it as it is written in the reversed way so we want to reverse it back to normal
     */
    public void setSourcePortNo(int sourcePortNo)
    {
        int sourcePortNoReversed = 0;
        while(sourcePortNo != 0)
        {
            int remainder = sourcePortNo % 10;
            sourcePortNoReversed = sourcePortNoReversed * 10 + remainder;
            sourcePortNo = sourcePortNo/10;
        }
        this.sourcePortNo = sourcePortNoReversed;
    }

    public int getDestinationPortNo() {
        return destinationPortNo;
    }

    public void setDestinationPortNo(int destinationPortNo) {
        this.destinationPortNo = destinationPortNo;
    }

    public int getLengthInBytes() {
        return lengthInBytes;
    }

    public void setLengthInBytes(int lengthInBytes) {
        this.lengthInBytes = lengthInBytes;
    }

    public String getChecksum() {
        return checksum;
    }

    public void setChecksum(String checksum) {
        this.checksum = "0x"+checksum;
    }

    public DBUdp(String transportHeaderName, String udpHeaderBytes)
    {
        super(transportHeaderName,udpHeaderBytes);
    }
    /**
     * Input : Packet received
     * Output : void
     * Description :
     * This function takes the packet and convert it to Udp packet
     * and then sets every part of the header of the udp in the DBUdp
     * and also it sets the sending and receiving ports of the DBFrame object
     */

    public void getTransportHeaderDetails(Packet packet,Frame_DB frame)
    {
        UdpPacket udpPacket = packet.get(UdpPacket.class);
        UdpPacket.UdpHeader udpHeader = udpPacket.getHeader();
        System.out.println(udpHeader.toString());
        //The payload of the tcp which is the data
        String payloadOfUdp = udpPacket.getPayload().toString();
        System.out.println(payloadOfUdp);
        setSourcePortNo(udpHeader.getSrcPort().valueAsInt());
        setDestinationPortNo(udpHeader.getDstPort().valueAsInt());
        setLengthInBytes(udpHeader.getLengthAsInt());
        setChecksum(Integer.toHexString(udpHeader.getChecksum() & 0xffff));
    }

    @Override
    public String toString() {
        return super.toString() + "\nDBUdp{" +
                "\nsourcePortNo=" + sourcePortNo +
                "\n, destinationPortNo=" + destinationPortNo +
                "\n, length=" + lengthInBytes +
                "\n, checksum=" + checksum +
                "\n}\n";
    }
}
