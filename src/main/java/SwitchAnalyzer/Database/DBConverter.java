package SwitchAnalyzer.Database;

import SwitchAnalyzer.Database.DBFrame;
import SwitchAnalyzer.Network.NetworkHeader;
import SwitchAnalyzer.Network.UDPHeader;
import SwitchAnalyzer.Sockets.PacketInfoGui;
import org.pcap4j.packet.*;

import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.Date;

public class DBConverter
{
    /**
     * Input : Information sent from the gui about the packet , packet received
     * Output : void
     * Description :
     * This function will convert the packet to DBFrame so, it can be stored in the database
     *      Steps:
     *      1. Create a new frame object
     *      2. Set the full packet as bytes
     *      3. Set a time stamp for this frame
     *      4. Set the network header based on its type (the type  passed by the gui)
     *      5. Set the transport header based on its type (the type  passed by the gui)
     *      6. Handle error in routing and crc checker "not done yet"
     */
    public static void convertToFrame(Packet packet)
    {
        DBFrame frame = new DBFrame();
        frame.setFullPacket(packet.getRawData());
        if(frame.getFullPacket()=="")
        {
            System.out.println(frame.getID() +" : "+packet.getRawData());
        }
        frame.setTimeStamp(new Date());
        if(frame.getTimeStamp() == null)
        {
            System.out.println(frame.getID() +" : Time stamp is not set");
        }
        DBNetworkHeader dbNetworkHeader = DBNetworkHeader.networkHeaderInit(packet);
        frame.setNetworkHeader(dbNetworkHeader);
        DBTransportHeader dbTransportHeader = DBTransportHeader.transportHeaderInit(packet);
        frame.setTransportHeader(dbTransportHeader);
//        frame = new DBFrame();
//        frame.setFullPacket(packet.getRawData());
//        frame.setTimeStamp(new Date());
//        DBNetworkHeader dbNetworkHeader = DBNetworkHeader.networkHeaderInit(packet);
//        frame.setNetworkHeader(dbNetworkHeader);
//        DBTransportHeader dbTransportHeader = DBTransportHeader.transportHeaderInit(packet);
//        frame.setTransportHeader(dbTransportHeader);
//        //System.out.println(frame.toString());
        DBInsert.insert(frame);
        //System.out.println(packet.contains(UdpPacket.class));

        //DBTransportHeader.transportHeaderInit(packetInfoGui,packet,frame);
        //know which Mac refers to which port to set the sending and receiving ports in the frame
//        getDstMac(packet);
//        getSrcMac(packet);
        //handle error in routing and crc checker
//        convertNetworkHeader(packetInfoGui,packet,frame);
//        convertTransportHeader(packetInfoGui,packet,frame);
    }

    /**
     * Input : Information sent from the gui about the packet , packet received , the frame object created by the function convertToDB
     * Output : void
     * Description :
     * This function will create an object based on the type of the network header
     * If the type is unknown the function will print that the type is unknown
     * Then it will set the details for that header
     * Finally it will set the header in the frame object
     */
//    private static void convertNetworkHeader(PacketInfoGui packetInfoGui , Packet packet,DBFrame frame)
//    {
//        try
//        {
//            DBNetworkHeader dbNetworkHeader = DBNetworkHeader.networkHeaderInit(packet);
//            //dbNetworkHeader.getNetworkHeaderDetails(packet);
//            frame.setNetworkHeader(dbNetworkHeader);
//        }
//        catch (Exception e)
//        {
//            System.out.println("The type of network header is unknown");
//        }
//    }
    /**
     * Input : Information sent from the gui about the packet , packet received , the frame object created by the function convertToDB
     * Output : void
     * Description :
     * This function will create an object based on the type of the transport header
     * If the type is unknown the function will print that the type is unknown
     * Then it will set the details for that header
     * Finally it will set the header in the frame object
     */
//    private static void convertTransportHeader(PacketInfoGui packetInfoGui , Packet packet,DBFrame frame)
//    {
//        try
//        {
//            DBTransportHeader dbTransportHeader = DBTransportHeader.transportHeaderInit(packet);
//            //dbTransportHeader.getTransportHeaderDetails(packet,frame);
//            frame.setTransportHeader(dbTransportHeader);
//        }
//        catch (Exception e)
//        {
//            System.out.println("The type of transport header is unknown");
//        }
//    }
    /**
     * Input : packet received
     * Output : String
     * Description :
     * This function will retrieve the destination address of the packet received "not used yet"
     */
    private static String getDstMac(Packet packet)
    {
        String dstMac = packet.get(EthernetPacket.class).getHeader().getDstAddr().toString();
        System.out.println(dstMac);
        return dstMac;
    }
    /**
     * Input : packet received
     * Output : String
     * Description :
     * This function will retrieve the source address of the packet received "not used yet"
     */
    private static String getSrcMac(Packet packet)
    {
        String srcMac = packet.get(EthernetPacket.class).getHeader().getSrcAddr().toString();
        System.out.println(srcMac);
        return srcMac;
    }

}
