package SwitchAnalyzer.miscellaneous;

import SwitchAnalyzer.Collectors.*;
import SwitchAnalyzer.Commands.*;
import SwitchAnalyzer.Commands.MOM.*;
import SwitchAnalyzer.Commands.MOM.OnlyMOM.GetFramesForRun_MOM;
import SwitchAnalyzer.Commands.MOM.OnlyMOM.SaveSwitchCMD_MOM;
import SwitchAnalyzer.Commands.MOM.OnlyMOM.ShowHistoryCmd_MOM;
import SwitchAnalyzer.Commands.Master.*;
import SwitchAnalyzer.Commands.Node.*;
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
        commandClasses.add(EndRunCmd_MOM.class);
        commandClasses.add(ShowHistoryCmd_MOM.class);
        commandClasses.add(SaveSwitchCMD_MOM.class);
        commandClasses.add(GetFramesForRun_MOM.class);
        MOMinitStub();
        initDefaultPortPair();

    }

    public static void MOMinitStub()
    {
        MasterOfHPC master1 = new MasterOfHPC(0,"Cluster1");
        MasterOfHPC master2 = new MasterOfHPC(1,"Cluster2");
        GlobalVariable.portHpcMap.put(1, master1);
        GlobalVariable.portHpcMap.put(2, master2);

        master1.childNodes.add(new MachineNode(0));
        //master1.childNodes.add(new MachineNode(1));
        master2.childNodes.add(new MachineNode(0));
        //master2.childNodes.add(new MachineNode(1));

        MainHandler_MOM.masterOfMasters = new MOM();
        MainHandler_MOM.masterOfMasters.HPCs.add(master1);
        MainHandler_MOM.masterOfMasters.HPCs.add(master2);
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

    public static final String START_RUN_CMD_MASTER_IDX = "0";
    public static final String RETRIEVE_CMD_MASTER_IDX = "1";
    public static final String STOP_RETRIEVE_CMD_MASTER_IDX = "2";
    public static final String STOP_RUN_CMD_MASTER_IDX = "3";

    public static final String RESUME_RUN_CMD_MASTER_IDX = "4";
    public static final String START_RUN_ALL_CMD_MASTER_IDX = "5";
    public static final String END_RUN_CMD_MASTER_IDX = "6";

    public static void initMapsMaster()
    {
        commandClassesMaster.add(StartRunCommand_Master.class);
        commandClassesMaster.add(RetrieveCmd_Master.class);
        commandClassesMaster.add(StopRetrieveCmd_Master.class);
        commandClassesMaster.add(StopRunCmdMaster.class);
        commandClassesMaster.add(ResumeRunCmd_Master.class);
        commandClassesMaster.add(StartRunALL.class);
        commandClassesMaster.add(EndCmd_Master.class);
        collectors.put(NamingConventions.rates, new RatesCollectorMaster());
        collectors.put(NamingConventions.packetLoss, new PLossCollectorMaster());
        MasterinitStub();
    }

    public static void MasterinitStub()
    {
        MasterOfHPC master1 = new MasterOfHPC(0,"Master1");
        MasterOfHPC master2 = new MasterOfHPC(1,"Cluster2");
        MachineNode machine1 = new MachineNode(0);
        //MachineNode machine2 = new MachineNode(1);
        MachineNode machine3 = new MachineNode(1);
        //MachineNode machine4 = new MachineNode(1);

        GlobalVariable.portHpcMap.put(1, master1);
        GlobalVariable.portHpcMap.put(2, master2);

        master1.childNodes.add(machine1);
        //master1.childNodes.add(machine2);
        master2.childNodes.add(machine3);
        //master2.childNodes.add(machine4);

        try
        {
            master1.HPCMacAddr = Builder.buildMacAddress("54:EE:75:DF:82:C4");
            master1.HPCIp = Builder.buildIpV4Address("192.168.1.100");

            master2.HPCMacAddr = Builder.buildMacAddress("54:EE:75:DF:82:C4");
            master2.HPCIp = Builder.buildIpV4Address("192.168.1.100");

            machine1.nodeMacAddress = Builder.buildMacAddress("8C:16:45:21:C5:E7");
            machine1.nodeIp = Builder.buildIpV4Address("192.168.1.35");

            //machine2.nodeMacAddress = Builder.buildMacAddress("00:00:00:00:00:01");
            //  machine2.nodeIp = Builder.buildIpV4Address("192.168.1.100");

            machine3.nodeMacAddress = Builder.buildMacAddress("3C:2C:30:9B:3B:90");
            machine3.nodeIp = Builder.buildIpV4Address("192.168.1.101");

            //machine4.nodeMacAddress = Builder.buildMacAddress("00:00:00:00:00:01");
            //machine4.nodeIp = Builder.buildIpV4Address("192.168.1.100");
        }
        catch (Exception ignored){}

        MainHandler_Master.master = master1;
    }

    public static final String START_RUN_NODE_IDX = "0";
    public static final String START_RUN_ALL_NODE_IDX = "1";
    public static final String STOP_RUN_CMD_NODE_IDX = "2";
    public static final String RESUME_RUN_CMD_NODE_IDX = "3";
    public static final String END_RUN_CMD_NODE_IDX = "4";
    public static void initMapsNode()
    {
        executorHashMap.put(NamingConventions.rates, new RateExecutor());
        executorHashMap.put(NamingConventions.packetLoss, new PacketLossExecutor());
        commandClassesNode.add(StartRunCommand_Node.class);
        commandClassesNode.add(StartRunAllNodes.class);
        commandClassesNode.add(StopRunCmd_Node.class);
        commandClassesNode.add(ResumeRunCmd_Node.class);
        commandClassesNode.add(EndRunCmd_Node.class);
        nodeInitStub();
    }

    public static void nodeInitStub()
    {
        MasterOfHPC master1 = new MasterOfHPC(0 ,"Cluster 1");
        MasterOfHPC master2 = new MasterOfHPC(1,"Cluster 2");
        MachineNode machine1 = new MachineNode(0);
        //MachineNode machine2 = new MachineNode(1);
        MachineNode machine3 = new MachineNode(0);
        //MachineNode machine4 = new MachineNode(1);

        GlobalVariable.portHpcMap.put(1, master1);
        GlobalVariable.portHpcMap.put(2, master2);

        master1.childNodes.add(machine1);
        // master1.childNodes.add(machine2);
        master2.childNodes.add(machine3);
        // master2.childNodes.add(machine4);

        try
        {
            master1.HPCMacAddr = Builder.buildMacAddress("54:EE:75:DF:82:C4");
            master1.HPCIp = Builder.buildIpV4Address("192.168.1.100");

            master2.HPCMacAddr = Builder.buildMacAddress("54:EE:75:DF:82:C4");
            master2.HPCIp = Builder.buildIpV4Address("192.168.1.100");

            machine1.nodeMacAddress = Builder.buildMacAddress("8C:16:45:21:C5:E7");
            machine1.nodeIp = Builder.buildIpV4Address("192.168.1.35");

//            machine2.nodeMacAddress = Builder.buildMacAddress("00:00:00:00:00:01");
//            machine2.nodeIp = Builder.buildIpV4Address("192.168.1.100");

            machine3.nodeMacAddress = Builder.buildMacAddress("3C:2C:30:9B:3B:90");
            machine3.nodeIp = Builder.buildIpV4Address("192.168.1.101");

//            machine4.nodeMacAddress = Builder.buildMacAddress("00:00:00:00:00:01");
//            machine4.nodeIp = Builder.buildIpV4Address("192.168.1.100");
        }
        catch (Exception ignored){}

        MainHandler_Node.node = machine1;
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
