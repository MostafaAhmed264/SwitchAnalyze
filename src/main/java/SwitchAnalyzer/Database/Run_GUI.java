package SwitchAnalyzer.Database;

import java.util.Map;

public class Run_GUI implements Run
{
    public String switchName;
    public String runNo;
    public Map<String,String> runDetails;
    public Run_GUI(String runNo, Map<String, String> runDetails) {
        this.runNo = runNo;
        this.runDetails = runDetails;
    }
    public Run_GUI(String switchName, String runNo) {
        this.switchName = switchName;
        this.runNo = runNo;
    }
}
