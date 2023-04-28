package SwitchAnalyzer.Machines;

import SwitchAnalyzer.ClusterConfigurations.ClusterConfiguartions;
import SwitchAnalyzer.ClusterConfigurations.MachineConfigurations;
import SwitchAnalyzer.miscellaneous.GlobalVariable;

import java.util.ArrayList;

public class MOM {
    public ArrayList<MasterOfHPC> HPCs;
    public MachineNode machineNode;
    public MOM(MachineNode machineNode)
    {
        this.machineNode=machineNode;
        this.HPCs = new ArrayList<>();

    }

    public MOM() {

    }

    public void setHPCsInformation(ArrayList<ClusterConfiguartions>  clusterConfigs)
    {
        for(ClusterConfiguartions oneHPCconfig:clusterConfigs)
        {
            MasterOfHPC master = new MasterOfHPC(oneHPCconfig.getCluster_Id(),oneHPCconfig.getCluster_name(),getMasterNode(oneHPCconfig.machines));
            master.setChildNodes(oneHPCconfig.machines);
            HPCs.add(master);
            GlobalVariable.portHpcMap.put(oneHPCconfig.getPort_id(),master);
        }
    }
    public MachineNode getMasterNode(ArrayList<MachineConfigurations> machineConfigs){
        for(MachineConfigurations machineConfig :machineConfigs)
        {
            if(machineConfig.Is_master())
            {
                return new MachineNode(machineConfig.getMachine_id(),machineConfig.getIp(),machineConfig.getMac());
            }
        }
        return null;
    }
}
