package SwitchAnalyzer.miscellaneous;

import SwitchAnalyzer.Collectors.*;
import SwitchAnalyzer.Commands.*;
import SwitchAnalyzer.Machines.MOM;
import SwitchAnalyzer.Machines.MachineNode;
import SwitchAnalyzer.Machines.MasterOfHPC;
import SwitchAnalyzer.MainHandler_MOM;
import SwitchAnalyzer.MainHandler_Master;
import SwitchAnalyzer.MainHandler_Node;
import SwitchAnalyzer.NamingConventions;
import SwitchAnalyzer.Network.*;
import SwitchAnalyzer.Network.ErrorDetection.CRC;
import SwitchAnalyzer.Network.ErrorDetection.None;
import SwitchAnalyzer.Network.HardwareObjects.SwitchPort;
import SwitchAnalyzer.Network.HardwareObjects.SwitchPortConfig;
import SwitchAnalyzer.Network.HardwareObjects.SwitchPortPair;
import SwitchAnalyzer.UtilityExecution.IExecutor;
import SwitchAnalyzer.UtilityExecution.PacketLossExecutor;
import SwitchAnalyzer.UtilityExecution.RateExecutor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SystemMaps
{
    public static ArrayList<Class<? extends ICommand>> commandClasses = new ArrayList<>();
    public static ArrayList<Class<? extends ICommandMaster>> commandClassesMaster = new ArrayList<>();
    public static HashMap<String, Collector> collectors = new HashMap<>();
    public static ArrayList<Class<? extends ICommandNode>> commandClassesNode = new ArrayList<>();
    public static HashMap<String, IExecutor> executorHashMap= new HashMap<>();

    public static void initMapsMOM()
    {
        collectors.put(NamingConventions.rates,new RatesCollectorMOM());
        collectors.put(NamingConventions.packetLoss,new PLossCollectorMOM());
        commandClasses.add(StartRunCommand_MOM.class);
        commandClasses.add(RetrieveCmd_MOM.class);
        commandClasses.add(StopRetrieveCmd_MOM.class);
        commandClasses.add(StopRunCmd_MOM.class);
        commandClasses.add(ResumeRunCmd_MOM.class);
        commandClasses.add(ResumeRunCmd_MOM.class);
        commandClasses.add(EndRunCmd_MOM.class);
        commandClasses.add(ShowHistoryCmd_MOM.class);
        commandClasses.add(SaveSwitchCMD_MOM.class);
        commandClasses.add(GetFramesForRun_MOM.class);
        initDefaultPortPair();
    }

    public static void initDefaultPortPair()
    {
        SwitchPortConfig config = new SwitchPortConfig( GlobalVariable.defaultPacketInfos, GlobalVariable.defUtilities, 500, "send");
        for (MasterOfHPC master : MainHandler_MOM.masterOfMasters.HPCs)
        {
            if (master.getHPCID() % 2 == 0)
            {
                SwitchPort fromPort = new SwitchPort(config , master.getHPCID());
                SwitchPortPair switchPortPair = new SwitchPortPair(fromPort , master.getHPCID()-1);
                GlobalVariable.defPairs.add(switchPortPair);
            }
        }
    }
    public static void initMapsMaster()
    {
        commandClassesMaster.add(StartRunCommand_Master.class);
        commandClassesMaster.add(RetrieveCmd_Master.class);
        commandClassesMaster.add(StopRetrieveCmd_Master.class);
        commandClassesMaster.add(StopRunCmdMaster.class);
        commandClassesMaster.add(ResumeRunCmd_Master.class);
        commandClassesMaster.add(StartRecieve_Master.class);
        commandClassesMaster.add(EndCmd_Master.class);
        collectors.put(NamingConventions.rates, new RatesCollectorMaster());
        collectors.put(NamingConventions.packetLoss, new PLossCollectorMaster());
    }

    public static void initMapsNode()
    {
        executorHashMap.put(NamingConventions.rates, new RateExecutor());
        executorHashMap.put(NamingConventions.packetLoss, new PacketLossExecutor());
        commandClassesNode.add(StartRunCommand_Node.class);
        commandClassesNode.add(RetrieveCmd_Node.class);
        commandClassesNode.add(StopRetrieveCmd_Node.class);
        commandClassesNode.add(StopRunCmd_Node.class);
        commandClassesNode.add(ResumeRunCmd_Node.class);
        commandClassesNode.add(StartRecieve_Node.class);
        commandClassesNode.add(EndRunCmd_Node.class);
    }

    public static void clear()
    {
        commandClasses.clear();
        commandClassesMaster.clear();
        collectors.clear();
        commandClassesNode.clear();
        executorHashMap.clear();
    }

    public static void initPortInfoMap(Map <String , Header> map)
    {
        map.put("udp",new UDPHeader());
        map.put("tcp",new TCPHeader());
        map.put("ipv4",new IPV4Header());
        map.put("ipv6",new IPV6Header());
        map.put("Ethernet" ,new EthernetHeader());
        map.put("CRC", new CRC());
        map.put("None", new None());
    }
}
