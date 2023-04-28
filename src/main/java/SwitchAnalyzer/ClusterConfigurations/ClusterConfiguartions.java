package SwitchAnalyzer.ClusterConfigurations;

import java.util.ArrayList;

public class ClusterConfiguartions {

    public String cluster_name;

    public String cluster_Id;
    //the mapped port id to a specific HPC
    public String port_id;
    public ArrayList<MachineConfigurations> machines;

    public String getCluster_name() {
        return cluster_name;
    }

    public int getCluster_Id() {
        return Integer.parseInt(cluster_Id);
    }

    public int getPort_id() {
        return Integer.parseInt(port_id);
    }
}
