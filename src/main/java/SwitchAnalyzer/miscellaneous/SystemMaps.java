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

        MOMinitStub();
    }

    public static void MOMinitStub()
    {
        MasterOfHPC master1 = new MasterOfHPC(0);
        MasterOfHPC master2 = new MasterOfHPC(1);
        GlobalVariable.portHpcMap.put(1, master1);
        GlobalVariable.portHpcMap.put(2, master2);

        master1.childNodes.add(new MachineNode(0));
//        master1.childNodes.add(new MachineNode(1));
        master2.childNodes.add(new MachineNode(0));
//        master2.childNodes.add(new MachineNode(1));

        MainHandler_MOM.masterOfMasters = new MOM();
        MainHandler_MOM.masterOfMasters.HPCs.add(master1);
        MainHandler_MOM.masterOfMasters.HPCs.add(master2);
    }


    public static void initMapsMaster()
    {
        commandClassesMaster.add(StartRunCommand_Master.class);
        commandClassesMaster.add(RetrieveCmd_Master.class);
        commandClassesMaster.add(StopRetrieveCmd_Master.class);
        commandClassesMaster.add(StopRunCmdMaster.class);
        commandClassesMaster.add(ResumeRunCmd_Master.class);
        collectors.put(NamingConventions.rates, new RatesCollectorMaster());
        collectors.put(NamingConventions.packetLoss, new PLossCollectorMaster());

        MasterinitStub();
    }

    public static void MasterinitStub()
    {
        MasterOfHPC master1 = new MasterOfHPC(0);
        MasterOfHPC master2 = new MasterOfHPC(1);
        MachineNode machine1 = new MachineNode(0);
        MachineNode machine2 = new MachineNode(1);
        MachineNode machine3 = new MachineNode(0);
        MachineNode machine4 = new MachineNode(1);

        GlobalVariable.portHpcMap.put(1, master1);
        GlobalVariable.portHpcMap.put(2, master1);

        master1.childNodes.add(machine1);
        master1.childNodes.add(machine2);
        master2.childNodes.add(machine3);
        master2.childNodes.add(machine4);

        try
        {
            master1.HPCMacAddr = Builder.buildMacAddress("00:00:00:00:00:01");
            master1.HPCIp = Builder.buildIpV4Address("192.168.1.100");

            master2.HPCMacAddr = Builder.buildMacAddress("00:00:00:00:00:01");
            master2.HPCIp = Builder.buildIpV4Address("192.168.1.100");

            machine1.nodeMacAddress = Builder.buildMacAddress("00:00:00:00:00:01");
            machine1.nodeIp = Builder.buildIpV4Address("192.168.1.100");

            machine2.nodeMacAddress = Builder.buildMacAddress("00:00:00:00:00:01");
            machine2.nodeIp = Builder.buildIpV4Address("192.168.1.100");

            machine3.nodeMacAddress = Builder.buildMacAddress("00:00:00:00:00:01");
            machine3.nodeIp = Builder.buildIpV4Address("192.168.1.100");

            machine4.nodeMacAddress = Builder.buildMacAddress("00:00:00:00:00:01");
            machine4.nodeIp = Builder.buildIpV4Address("192.168.1.100");
        }
        catch (Exception ignored){}

        MainHandler_Master.master = master1;
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
        nodeInitStub();
    }

    public static void nodeInitStub()
    {
        MasterOfHPC master1 = new MasterOfHPC(0);
        MasterOfHPC master2 = new MasterOfHPC(1);
        MachineNode machine1 = new MachineNode(0);
        MachineNode machine2 = new MachineNode(1);
        MachineNode machine3 = new MachineNode(0);
        MachineNode machine4 = new MachineNode(1);

        GlobalVariable.portHpcMap.put(1, master1);
        GlobalVariable.portHpcMap.put(2, master1);

        master1.childNodes.add(machine1);
        master1.childNodes.add(machine2);
        master2.childNodes.add(machine3);
        master2.childNodes.add(machine4);

        try
        {
            master1.HPCMacAddr = Builder.buildMacAddress("00:00:00:00:00:01");
            master1.HPCIp = Builder.buildIpV4Address("192.168.1.100");

            master2.HPCMacAddr = Builder.buildMacAddress("00:00:00:00:00:01");
            master2.HPCIp = Builder.buildIpV4Address("192.168.1.100");

            machine1.nodeMacAddress = Builder.buildMacAddress("00:00:00:00:00:01");
            machine1.nodeIp = Builder.buildIpV4Address("192.168.1.100");

            machine2.nodeMacAddress = Builder.buildMacAddress("00:00:00:00:00:01");
            machine2.nodeIp = Builder.buildIpV4Address("192.168.1.100");

            machine3.nodeMacAddress = Builder.buildMacAddress("00:00:00:00:00:01");
            machine3.nodeIp = Builder.buildIpV4Address("192.168.1.100");

            machine4.nodeMacAddress = Builder.buildMacAddress("00:00:00:00:00:01");
            machine4.nodeIp = Builder.buildIpV4Address("192.168.1.100");
        }
        catch (Exception ignored){}

        MainHandler_Node.node = machine1;
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
