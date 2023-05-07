package SwitchAnalyzer.Database;

import java.util.Map;

public class Run_DB implements Run
{
    public Long runno;
    public Map<String,String> rundetails;
    public Run_DB() { runno = DBConnect.getLastRun(); }


}
