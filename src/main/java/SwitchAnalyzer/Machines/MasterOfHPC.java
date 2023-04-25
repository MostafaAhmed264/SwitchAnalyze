package SwitchAnalyzer.Machines;

import SwitchAnalyzer.ClusterConfigurations.MachineConfigurations;

import java.util.ArrayList;

public class MasterOfHPC {


    public  ArrayList<MachineNode> childNodes = new ArrayList<MachineNode>();
    public HPC_INFO hpcInfo;
    //TODO: will be used when constructing packets to be sent

    // no need for HPC MAC address as every node whatever it's MOM,Master or Normal node has
    // Machine node object carrying it's information (Mac ,ip)
//    private MacAddress HPCMacAddr;
//    private Inet4Address HPCIp;
    public MachineNode machineNode=new MachineNode();

    public MasterOfHPC(int HPCID,String clusterName){
        hpcInfo =new HPC_INFO(HPCID,clusterName);
    }
    public MasterOfHPC(int HPCID ,String clusterName,MachineNode machineNode) {
        hpcInfo = new HPC_INFO(HPCID,clusterName);
        this.machineNode=machineNode;

    }
    public int getNoOfChilNodes() {
        return childNodes.size();
    }
    public int getHPCID() {
        return hpcInfo.HPCID;
    }
    public String getClusterName(){
        return hpcInfo.clusterName;
    }
    public void setChildNodes(ArrayList<MachineConfigurations> machineConfigs) {
        for(MachineConfigurations machineConfig :machineConfigs){
            if(!(machineConfig.Is_master()))
            childNodes.add(new MachineNode(machineConfig.getMachine_id(), machineConfig.getIp(),machineConfig.getMac()));
        }
    }



}