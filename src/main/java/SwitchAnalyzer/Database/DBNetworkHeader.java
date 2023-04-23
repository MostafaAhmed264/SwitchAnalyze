package SwitchAnalyzer.Database;

import SwitchAnalyzer.Sockets.PacketInfoGui;
import org.pcap4j.packet.IpV4Packet;
import org.pcap4j.packet.IpV6Packet;
import org.pcap4j.packet.Packet;

public class DBNetworkHeader
{
    private String networkHeaderName = "";
    private String networkFullHeader = "";

    public DBNetworkHeader(){}
    public DBNetworkHeader(String networkHeaderName, String networkFullHeader) {
        this.networkHeaderName = networkHeaderName;
        this.networkFullHeader = networkFullHeader;
    }

    public String getNetworkHeaderName() {
        return networkHeaderName;
    }

    public void setNetworkHeaderName(String networkHeaderName) {
        this.networkHeaderName = networkHeaderName;
    }

    public String getNetworkFullHeader() {
        return networkFullHeader;
    }
    /**
     * Input : bytes of the network header
     * Output : void
     * Description :
     * This function will convert the bytes of the network header to string and then set it in the object
     */
    public void setNetworkFullHeader(byte[] networkHeaderBytes)
    {
        this.networkFullHeader = networkHeaderBytesToString(networkHeaderBytes);
    }
    /**
     * Input : bytes of the network header
     * Output : String
     * Description :
     * This function will convert the bytes of the network header to string and return that string
     * This function is used when creating an object of network header type
     */
    private static String networkHeaderBytesToString(byte[] networkHeaderBytes) {
        StringBuilder sb =new StringBuilder();
        for (int i = 0; i < networkHeaderBytes.length; i++)
        {
            sb.append(String.format("%02X", networkHeaderBytes[i])).append(" ");
        }
        return sb.toString();
    }
    /**
     * Input : Information sent from the gui about the packet , packet received
     * Output : Object based on the type of the network header
     * Description :
     * This function will see what is the type of the network header based on what the gui sent
     * and creates an object of this type and set for it its name and its header bytes as string
     */
    public static DBNetworkHeader networkHeaderInit(Packet packet)
    {
//        if(packetInfoGui.networkHeader=="ipv4")
//        {
//            byte [] ipv4HeaderBytes = packet.get(IpV4Packet.class).getHeader().getRawData();
//            String ipv4HeaderString = networkHeaderBytesToString(ipv4HeaderBytes);
//            return (T) new DBIpv4("Ipv4",ipv4HeaderString);
//        }
//        else if (packetInfoGui.networkHeader=="ipv6")
//        {
//            byte [] ipv6HeaderBytes = packet.get(IpV6Packet.class).getHeader().getRawData();
//            StringBuilder sb =new StringBuilder();
//            String ipv6HeaderString = networkHeaderBytesToString(ipv6HeaderBytes);
//            return (T) new DBIpv6("Ipv6", ipv6HeaderString);
//        }
//        return null;
//        String networkHeaderBytes = networkHeaderBytesToString(setNetworkHeaderDetails(packetInfoGui,packet));
//        String networkHeaderName = packetInfoGui.networkHeader;
//        return new DBNetworkHeader(networkHeaderName,networkHeaderBytes);
        String networkHeaderName;
        if(packet.contains(IpV4Packet.class))
        {
            networkHeaderName = "Ipv4";
        }
        else if(packet.contains(IpV6Packet.class))
        {
            networkHeaderName = "Ipv6";
        }
        else
        {
         networkHeaderName = "";
        }
        String networkHeaderBytes = networkHeaderBytesToString(setNetworkHeaderDetails(networkHeaderName,packet));
        return new DBNetworkHeader(networkHeaderName,networkHeaderBytes);
    }
//    public static void networkHeaderInit(PacketInfoGui packetInfoGui,Packet packet,DBFrame frame)
//    {
//        frame.getNetworkHeader().setNetworkHeaderName(packetInfoGui.networkHeader);
//        frame.getNetworkHeader().setNetworkFullHeader(setNetworkHeaderDetails(packetInfoGui.networkHeader,packet));
//    }
    private static byte [] setNetworkHeaderDetails(String networkHeaderName, Packet packet)
    {
        byte [] networkHeaderBytes = new byte[0];
        if(networkHeaderName == "Ipv4")
        {
            networkHeaderBytes = packet.get(IpV4Packet.class).getHeader().getRawData();
        }
        else if (networkHeaderName == "Ipv6")
        {
            networkHeaderBytes = packet.get(IpV6Packet.class).getHeader().getRawData();
        }
        return networkHeaderBytes;
    }
    /**
     * Input : Packet received
     * Output : void
     * Description :
     * This function will be overridden by the types of the network header
     * This function is used to get the details of the network header and set it in the object of its type
     */
    //public abstract void getNetworkHeaderDetails(Packet packet);

    @Override
    public String toString() {
        return "DBNetworkHeader{" +
                "\nnetworkHeaderName='" + networkHeaderName + '\'' +
                "\n, networkFullHeader='" + networkFullHeader + '\'' +
                "\n}";
    }
}

