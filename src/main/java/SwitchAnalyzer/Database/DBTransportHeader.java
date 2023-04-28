package SwitchAnalyzer.Database;

import org.pcap4j.packet.Packet;
import org.pcap4j.packet.TcpPacket;
import org.pcap4j.packet.UdpPacket;

public class DBTransportHeader
{
    private String transportHeaderName;
    private String transportFullHeader;

    public DBTransportHeader(){}
    public DBTransportHeader(String transportHeaderName, String transportFullHeader) {
        this.transportHeaderName = transportHeaderName;
        this.transportFullHeader = transportFullHeader;
    }

    public String getTransportHeaderName() {
        return transportHeaderName;
    }

    public void setTransportHeaderName(String transportHeaderName) {
        this.transportHeaderName = transportHeaderName;
    }

    public String getTransportFullHeader() {
        return transportFullHeader;
    }
    /**
     * Input : bytes of the network header
     * Output : void
     * Description :
     * This function will convert the bytes of the network header to string and then set it in the object
     */
    public void setTransportFullHeader(byte[] transportHeaderBytes) {
        StringBuilder sb =new StringBuilder();
        for (int i = 0; i < transportHeaderBytes.length; i++)
        {
            sb.append(String.format("%02X", transportHeaderBytes[i])).append(" ");
        }
        this.transportFullHeader = sb.toString();
    }
    /**
     * Input : bytes of the transport header
     * Output : String
     * Description :
     * This function will convert the bytes of the transport header to string and return that string
     * This function is used when creating an object of transport header type
     */
    private static String transportHeaderBytesToString(byte[] transportHeaderBytes) {
        StringBuilder sb =new StringBuilder();
        for (int i = 0; i < transportHeaderBytes.length; i++)
        {
            sb.append(String.format("%02X", transportHeaderBytes[i])).append(" ");
        }
        return sb.toString();
    }
    /**
     * Input : Information sent from the gui about the packet , packet received
     * Output : Object based on the type of the transport header
     * Description :
     * This function will see what is the type of the transport header based on what the gui sent
     * and creates an object of this type and set for it its name and its header bytes as string
     */
    public static DBTransportHeader transportHeaderInit(Packet packet)
    {
//        if(packetInfoGui.transportHeader=="tcp")
//        {
//            byte [] tcpHeaderBytes = packet.get(TcpPacket.class).getHeader().getRawData();
//            String tcpHeaderString = transportHeaderBytesToString(tcpHeaderBytes);
//            return (T) new DBTcp("Ipv4",tcpHeaderString);
//        }
//        else if (packetInfoGui.transportHeader=="udp")
//        {
//            byte [] udpHeaderBytes = packet.get(UdpPacket.class).getHeader().getRawData();
//            String udpHeaderString = transportHeaderBytesToString(udpHeaderBytes);
//            return (T) new DBUdp("Ipv4",udpHeaderString);
//        }
//        return null;
//        String transportHeaderBytes = transportHeaderBytesToString(setTransportHeaderDetails(packetInfoGui,packet));
//        String transportHeaderName = packetInfoGui.transportHeader;
//        return new DBTransportHeader(transportHeaderName,transportHeaderBytes);
        String transportHeaderName;
        if(packet.contains(TcpPacket.class))
        {
            transportHeaderName = "Tcp";
        }
        else if(packet.contains(UdpPacket.class))
        {
            transportHeaderName = "Udp";
        }
        else
        {
            transportHeaderName = "";
        }
        String transportHeaderBytes = transportHeaderBytesToString(setTransportHeaderDetails(transportHeaderName,packet));
        return new DBTransportHeader(transportHeaderName,transportHeaderBytes);
    }
//    public static void transportHeaderInit(PacketInfoGui packetInfoGui,Packet packet,DBFrame frame)
//    {
//        frame.getTransportHeader().setTransportHeaderName(packetInfoGui.transportHeader);
//        frame.getTransportHeader().setTransportFullHeader(setTransportHeaderDetails(packetInfoGui.transportHeader,packet));
//
//    }
    private static byte [] setTransportHeaderDetails(String transportHeaderName,Packet packet)
    {
        byte [] transportHeaderBytes = new byte[0];
        if(transportHeaderName=="Tcp")
        {
            transportHeaderBytes = packet.get(TcpPacket.class).getHeader().getRawData();
        }
        else if (transportHeaderName=="Udp")
        {
            transportHeaderBytes = packet.get(UdpPacket.class).getHeader().getRawData();
        }
        return transportHeaderBytes;
    }
    /**
     * Input : Packet received , Frame created by the convertToFrame in DBConverter
     * Output : void
     * Description :
     * This function will be overridden by the types of the transport header
     * This function is used to get the details of the transport header and set it in the object of its type
     * This function also set the sending and receiving ports in the frame object
     */
    //public abstract void getTransportHeaderDetails(Packet packet , DBFrame frame);

    @Override
    public String toString() {
        return "DBTransportHeader{" +
                "\ntransportHeaderName='" + transportHeaderName + '\'' +
                "\n, transportFullHeader='" + transportFullHeader + '\'' +
                "\n}";
    }
}
