package SwitchAnalyzer.Database;

import java.util.HashMap;
public class DBRun {
    private long runno;
    public Long runNo;
    public HashMap<String,String> rundetails;
    public HashMap<String,String> runDetails;
    public DBRun() {runno = DBConnect.getLastRun();}
    public void setRunNo(long runNo) {
        this.runno = runNo;
    }
    public long getRunNo() {
        return runno;
    }
    public void setRunno_DBInsert(){ runno = DBConnect.getLastRun(); }
}
