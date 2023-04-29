package SwitchAnalyzer.Commands.Node;

import SwitchAnalyzer.Commands.ICommandNode;
import SwitchAnalyzer.MainHandler_Master;
import SwitchAnalyzer.MapPacketInfo;
import SwitchAnalyzer.Network.FrameReciever;
import SwitchAnalyzer.Network.HardwareObjects.SwitchPortConfig;
import SwitchAnalyzer.Network.PacketInfo;
import SwitchAnalyzer.Network.SendThreadsHandler;
import SwitchAnalyzer.ProduceData_Node;
import SwitchAnalyzer.Sockets.PacketInfoGui;
import SwitchAnalyzer.UtilityExecution.UtilityExecutor;
import SwitchAnalyzer.miscellaneous.SystemMaps;


/*
    OPENS SENDING THREADS
 */
public class StartRunCommand_Node extends ICommandNode
{
    SwitchPortConfig config;
    public int toPortID;

    public StartRunCommand_Node(int ID) { this.machineID = ID; }
    public StartRunCommand_Node(SwitchPortConfig config, int ID, int toPortID)
    {
        this.machineID = ID;
        this.config = config;
        this.toPortID = toPortID;
        distNoPackets();
    }

    public void distNoPackets()
    {
        config.rate = config.rate/ MainHandler_Master.master.getNoOfChilNodes();
        long num;
        for (PacketInfoGui packetInfo : config.packetInfos)
        {
            num = packetInfo.numberOfPackets;
            packetInfo.numberOfPackets = num / MainHandler_Master.master.getNoOfChilNodes();
        }
    }

    @Override
    /*
        OPENS SENDING THREADS IF ANY
     */
    public void processCmd()
    {
        openSendThreads();
    }

    public void openSendThreads()
    {
        for (PacketInfoGui packetInfo : config.packetInfos)
        {
            SendThreadsHandler.addToPacketInfoList((PacketInfo) new MapPacketInfo().map(packetInfo));
        }
        SendThreadsHandler.sendToSelectedPort(toPortID, config.rate, config.duration);
    }
}
