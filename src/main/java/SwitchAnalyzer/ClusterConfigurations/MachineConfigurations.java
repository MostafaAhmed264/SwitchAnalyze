package SwitchAnalyzer.ClusterConfigurations;

import org.pcap4j.util.MacAddress;

public class MachineConfigurations {
    public String machine_id;
    public String ip;
    public String mac;
    public String is_master;

    public int getMachine_id() {
        return Integer.parseInt(machine_id);
    }

    public String getIp() {
        return ip;
    }

    public MacAddress getMac() {
        return toMacAdrress(mac);

    }
    public MacAddress toMacAdrress(String stringMacAddress){
        MacAddress retrievedMacAddress =MacAddress.getByName(stringMacAddress);
        return retrievedMacAddress;
    }

    public boolean Is_master() {
        if(is_master.equals("True"))
            return true;

        return false;
    }
}
