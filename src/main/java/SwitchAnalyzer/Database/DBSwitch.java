package SwitchAnalyzer.Database;

public class DBSwitch
{
    private String switchName ="";
    private long totalNoOfPorts = -1;

    public DBSwitch(String switchName, long totalNoOfPorts) {
        this.switchName = switchName;
        this.totalNoOfPorts = totalNoOfPorts;
    }

    public String getSwitchName() {
        return switchName;
    }

    public void setSwitchName(String switchName) {
        this.switchName = switchName;
    }

    public long getTotalNoOfPorts() {
        return totalNoOfPorts;
    }

    public void setTotalNoOfPorts(long totalNoOfPorts) {
        this.totalNoOfPorts = totalNoOfPorts;
    }
}
