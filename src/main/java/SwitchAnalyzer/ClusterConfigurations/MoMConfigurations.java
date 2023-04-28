package SwitchAnalyzer.ClusterConfigurations;

import org.pcap4j.util.MacAddress;

import java.util.ArrayList;

public class MoMConfigurations {
    public String Master_Ip;
    public String Master_Mac;
    public ArrayList<ClusterConfiguartions> cluster;

    //TODO : add a new field in the JSon (machine id) for the MOM

    //String machine_id;

    public String getMaster_Ip() {
        return Master_Ip;
    }

    public MacAddress getMaster_Mac() {
        return MacAddress.getByName(Master_Mac);
    }

}
