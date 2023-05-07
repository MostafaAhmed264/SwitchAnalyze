package SwitchAnalyzer.Database;

import java.util.Map;

public class DBRun {
    private Long runno;
    public String runNo;
    public Map<String,String> rundetails;
    public Map<String,String> runDetails;
    public DBRun() {runno = DBConnect.getLastRun();}

    public DBRun(String runNo, Map<String, String> runDetails) {
        this.runNo = runNo;
        this.runDetails = runDetails;
    }

    public Long getRunNo() {
        return runno;
    }
    public void setRunno_DBInsert(){ runno = DBConnect.getLastRun(); }
}