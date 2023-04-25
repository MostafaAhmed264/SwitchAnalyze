package SwitchAnalyzer.Machines;

import SwitchAnalyzer.Network.NIC_INFO;
import org.pcap4j.util.MacAddress;

import java.net.*;

/**
 * any information thats not needed to be put into kafka but is related to the machine node should be here
 */
public class MachineNode {
    public MachineInfo machineInfo;

    //TODO: will be used when constructing packets to be sent
    public MacAddress nodeMacAddress;
    public Inet4Address nodeIp;
    public MachineNode(){
        nodeMacAddress= NIC_INFO.detectMyMacAddress();
    }

    public MachineNode(int id)
    {
        machineInfo = new MachineInfo(id);
    }

    /**
    * this constructor for foreign machines that want to keep information about other nodes
     */
    public MachineNode(int id ,String ip,MacAddress macAddress){
        machineInfo = new MachineInfo(id);
        setNodeIp(ip);
        this.nodeMacAddress=macAddress;
    }

    public void setMachineID(int id){
        machineInfo =new MachineInfo(id);
    }

    public int getMachineID() {
        return machineInfo.machineID;
    }

    public MachineInfo getMachineInfo() {
        return machineInfo;
    }

    public MacAddress getNodeMacAddress() {
        return nodeMacAddress;
    }

    public Inet4Address getNodeIp() {
        return nodeIp;
    }




    public void setNodeIp(String nodeIp) {
        try {
            //TODO : set the decive ip then set the attribute

            this.nodeIp = (Inet4Address) Inet4Address.getByName(nodeIp);
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }
    }

}

