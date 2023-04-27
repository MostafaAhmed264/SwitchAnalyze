package SwitchAnalyzer.Database;

import java.util.ArrayList;

public class DBSwitch
{
    private String switchName ="";
    private long totalNoOfPorts = -1;
    private ArrayList<DBRun> switchruns;
    public DBSwitch(String switchName, long totalNoOfPorts) {
        this.switchName = switchName;
        this.totalNoOfPorts = totalNoOfPorts;
        switchruns = new ArrayList<DBRun>();
    }
    public String getSwitchName() { return switchName; }
    public void setSwitchName(String switchName) { this.switchName = switchName; }
    public long getTotalNoOfPorts() {return totalNoOfPorts; }
    public void setTotalNoOfPorts(long totalNoOfPorts) { this.totalNoOfPorts = totalNoOfPorts; }

    public void setSwitchRuns(ArrayList<DBRun> switchRuns) {
        this.switchruns = switchRuns;
    }

    @Override
    public String toString() {
        return "DBSwitch{" +
                "switchName='" + switchName + '\'' +
                ", totalNoOfPorts=" + totalNoOfPorts +
                ", switchRuns=" + switchruns +
                '}';
    }
}
