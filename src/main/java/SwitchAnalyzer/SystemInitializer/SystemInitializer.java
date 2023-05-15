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
import SwitchAnalyzer.miscellaneous.GlobalVariable;

import java.util.ArrayList;

import static SwitchAnalyzer.miscellaneous.GlobalVariable.consumer0;

public class SystemInitializer {
    public static void MoMinit(MachineNode myNode, MoMConfigurations momConfigurations) {
        myNode.setNodeIp(momConfigurations.getMaster_Ip());
        myNode.setMachineID(-1);
        MOM masterOfMasters = new MOM(myNode);
        masterOfMasters.setHPCsInformation(momConfigurations.cluster);
        MainHandler_MOM.masterOfMasters = masterOfMasters;
    }

    public static void MasterInit(MachineNode myNode, MachineConfigurations machineConfig, ClusterConfiguartions clusterConfig,MoMConfigurations momConfigurations)
    {
        myNode.setMachineID(machineConfig.getMachine_id());
        myNode.setNodeIp(machineConfig.getIp());
        MasterOfHPC master = new MasterOfHPC(clusterConfig.getCluster_Id(), clusterConfig.getCluster_name(), myNode);
        master.setChildNodes(clusterConfig.machines);
        master.setHPCmap(momConfigurations.cluster);
        MainHandler_Master.master = master;
    }

    public static void NodeInit(MachineNode myNode, MachineConfigurations machineConfig,MoMConfigurations momConfigurations)
    {
        myNode.setMachineID(machineConfig.getMachine_id());
        myNode.setNodeIp(machineConfig.getIp());
        myNode.setHPCmap(momConfigurations.cluster);
        MainHandler_Node.node = myNode;
    }
    public static void initPortHPCMap(MoMConfigurations momConfigurations)
    {
        for (ClusterConfiguartions config : momConfigurations.cluster)
        {
            ArrayList<MachineNode> nodes= new ArrayList<>();
            for (MachineConfigurations mConfig : config.machines)
            {
                MachineNode node = new MachineNode(mConfig.getMachine_id(),mConfig.ip,mConfig.getMac());
                nodes.add(node);
            }

            MasterOfHPC hpc = new MasterOfHPC(Integer.parseInt(config.cluster_Id), config.cluster_name);
            hpc.childNodes=nodes;
            GlobalVariable.portHpcMap.put(config.getPort_id(),hpc);
        }
    }
    public static void setConsumerGroups(MachineNode myNode){

        consumer0=myNode.nodeMacAddress.toString()+System.currentTimeMillis();
        GlobalVariable.consumer1=consumer0;
        GlobalVariable.consumer2=consumer0;
        GlobalVariable.consumer3=consumer0;
        GlobalVariable.consumer4=consumer0;
        GlobalVariable.consumer5=consumer0;
        GlobalVariable.consumer6=consumer0;

    }
}
