package SwitchAnalyzer.Machines;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * this is the information thats going to be produced into kafka
 * do not add anything here other than the information needed to be put into kafka
 */

public class HPC_INFO {
    public int HPCID;
    public String clusterName;
    public Map<String,String> map;
    public HPC_INFO(int id ,String clusterName)
    {
        this.HPCID = id;
        this.clusterName=clusterName;
        map =new ConcurrentHashMap<>();
    }
}
