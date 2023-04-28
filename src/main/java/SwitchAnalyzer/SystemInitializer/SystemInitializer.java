package SwitchAnalyzer.SystemInitializer;

import SwitchAnalyzer.ClusterConfigurations.ClusterConfiguartions;
import SwitchAnalyzer.ClusterConfigurations.MachineConfigurations;
import SwitchAnalyzer.ClusterConfigurations.MoMConfigurations;
import SwitchAnalyzer.Machines.MOM;
import SwitchAnalyzer.Machines.MachineNode;
import SwitchAnalyzer.Machines.MasterOfHPC;
import SwitchAnalyzer.MainHandler_MOM;
import SwitchAnalyzer.MainHandler_Master;
import SwitchAnalyzer.MainHandler_Node;

public class SystemInitializer {
    public static void MoMinit(MachineNode myNode, MoMConfigurations momConfigurations) {
        myNode.setNodeIp(momConfigurations.getMaster_Ip());
        myNode.setMachineID(-1);
        MOM masterOfMasters = new MOM(myNode);
        masterOfMasters.setHPCsInformation(momConfigurations.cluster);
        MainHandler_MOM.masterOfMasters = masterOfMasters;
    }

    public static void MasterInit(MachineNode myNode, MachineConfigurations machineConfig, ClusterConfiguartions clusterConfig)
    {
        myNode.setMachineID(machineConfig.getMachine_id());
        myNode.setNodeIp(machineConfig.getIp());
        MasterOfHPC master = new MasterOfHPC(clusterConfig.getCluster_Id(), clusterConfig.getCluster_name(), myNode);
        master.setChildNodes(clusterConfig.machines);
        MainHandler_Master.master = master;
    }

    public static void NodeInit(MachineNode myNode, MachineConfigurations machineConfig)
    {
        myNode.setMachineID(machineConfig.getMachine_id());
        myNode.setNodeIp(machineConfig.getIp());
        MainHandler_Node.node = myNode;
    }
}
