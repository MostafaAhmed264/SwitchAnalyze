package SwitchAnalyzer.Commands;

import SwitchAnalyzer.MainHandler_Master;
import SwitchAnalyzer.MapPacketInfo;
import SwitchAnalyzer.Network.HardwareObjects.SwitchPortConfig;
import SwitchAnalyzer.Network.PacketInfo;
import SwitchAnalyzer.Network.PacketSniffer;
import SwitchAnalyzer.Network.SendThreadsHandler;
import SwitchAnalyzer.ProduceData_Node;
import SwitchAnalyzer.Sockets.PacketInfoGui;
import SwitchAnalyzer.UtilityExecution.UtilityExecutor;
import SwitchAnalyzer.miscellaneous.SystemMaps;

public class StartRunCommand_Node extends ICommandNode
{
    SwitchPortConfig config;
    public int toPortID;

    StartRunCommand_Node (SwitchPortConfig config, int ID, int toPortID)
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
    public void processCmd()
    {
        addUtils();
        openProduceThread();
        openSendAndRecThreads();
    }

    public void addUtils()
    {
        for (String key : config.utilities)
        {
            UtilityExecutor.executors.add(SystemMaps.executorHashMap.get(key));
        }
    }

    public void openProduceThread()
    {
        Thread executeUtilitiesThread = new Thread (() ->
        {
            while(!UtilityExecutor.executors.isEmpty())
            {
                ProduceData_Node.produceData();
            }
        });
        if(!UtilityExecutor.executors.isEmpty()) { executeUtilitiesThread.start(); }
    }

    public void openSendAndRecThreads()
    {
        for (PacketInfoGui packetInfo : config.packetInfos)
        {
            SendThreadsHandler.addToPacketInfoList((PacketInfo) new MapPacketInfo().map(packetInfo));
        }
        SendThreadsHandler.sendToSelectedPort(toPortID, config.rate, config.duration);
    }

}
