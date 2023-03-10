package SwitchAnalyzer.Machines;

import SwitchAnalyzer.NamingConventions;
import org.pcap4j.util.MacAddress;

import java.net.Inet4Address;
import java.util.ArrayList;

public class MasterOfHPC {
    public  ArrayList<MachineNode> childNodes = new ArrayList<MachineNode>();
    public HPC_INFO hpcInfo;
    private Integer HPCID;
    private Integer noOfChilNodes;
    private Float CurrentOverallRate;
    private MacAddress HPCMacAddr;
    private Inet4Address HPCIp;
    private String status;
    public MasterOfHPC(int HPCID, int noOfMachines) {
        this.hpcInfo.HPCID= HPCID;
        this.noOfChilNodes = noOfMachines;
    }

    public int getNoOfChilNodes() {
        return noOfChilNodes;
    }
    public void setNoOfChilNodes(int noOfChilNodes) {
        this.noOfChilNodes = noOfChilNodes;
    }
    public MasterOfHPC(int HPCID) {
        this.HPCID = HPCID;
    }
    public void setCurrentOverallRate(float currentOverallRate) {
        CurrentOverallRate = currentOverallRate;
    }
    public int getHPCID() {
        return HPCID;
    }
    public void setHPCID(int HPCID) {
        this.HPCID = HPCID;
    }
    public float getCurrentOverallRate() {
        return Float.parseFloat(hpcInfo.map.get(NamingConventions.rates));
    }
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
}