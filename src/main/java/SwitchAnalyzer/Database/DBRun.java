package SwitchAnalyzer.Database;

import java.util.HashMap;
public class DBRun {
    private long runno;
    public HashMap<String,String> rundetails;
    public DBRun() {runno = DBConnect.getLastRun();}
    public void setRunNo(long runNo) {
        this.runno = runNo;
    }
    public long getRunNo() {
        return runno;
    }
}
