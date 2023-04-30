package SwitchAnalyzer.Database;

import java.util.ArrayList;

public class DBSwitch
{
    private String switchName ="";
    private Long totalNoOfPorts;
    private ArrayList<DBRun> switchruns;
    public ArrayList<DBRun> stats;
    public DBSwitch(String switchName, Long totalNoOfPorts) {
        this.switchName = switchName;
        this.totalNoOfPorts = totalNoOfPorts;
        switchruns = new ArrayList<DBRun>();
    }
    public String getSwitchName() { return switchName; }
    public void setSwitchName(String switchName) { this.switchName = switchName; }
    public Long getTotalNoOfPorts() {return totalNoOfPorts; }
    public void setTotalNoOfPorts(Long totalNoOfPorts) { this.totalNoOfPorts = totalNoOfPorts; }

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
