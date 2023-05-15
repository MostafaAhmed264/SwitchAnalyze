package SwitchAnalyzer.Database;

import java.util.ArrayList;

public class DBSwitch
{
    private String switchName ="";
    private Long totalnoofports;

    public String totalNoOfPorts;
    public ArrayList<DBRun> stats;
    public DBSwitch(String switchName, Long totalNoOfPorts) {
        this.switchName = switchName;
        this.totalnoofports = totalNoOfPorts;
    }
    public DBSwitch(String switchName, String totalNoOfPorts) {
        this.switchName = switchName;
        this.totalNoOfPorts = totalNoOfPorts;
    }
    public String getSwitchName() { return switchName; }
    public void setSwitchName(String switchName) { this.switchName = switchName; }
    public Long getTotalNoOfPorts() {return totalnoofports; }
}
