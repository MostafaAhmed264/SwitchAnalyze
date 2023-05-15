package SwitchAnalyzer.Database;

import com.datastax.driver.core.utils.UUIDs;

import java.util.HashMap;
import java.util.UUID;

/**
 * this Frame class represents the actually frame stored in kafka along with its ID
 * it implements JSON interface in order to be converted to and from JSON format.
 * @author ziad Fahmy
 * @since 2022-12-07
 */
public class DBFrame {
    private UUID id ;
    public String json;
    public String port;
    public enum Direction {INBOUND,OUTBOUND}
    public Direction directionEnum;
    public String Direction;
    public String direction;
    public HashMap<String, String> frameDetails;
    public HashMap<String, String> framedetails;
    private boolean errorInRouting;
    private boolean crcChecker;
    // Member variables for producing in kafka
    public String frame_json;
    public String bytes;
    public String runNo;
    public String switchName;
    public String table;
    public DBFrame() { id = UUIDs.timeBased(); }
    public DBFrame(String frame_json,Long runNo, String switchName) {
        this.frame_json = frame_json;
        this.switchName = switchName;
    }
    public DBFrame(String frame_json,String runNo, String switchName,String table) {
        this.frame_json = frame_json;
        this.switchName = switchName;
        this.runNo = runNo;
        this.table = table;
    }

    public UUID getID() { return id; }
    public boolean errorInRoutingExists() { return errorInRouting; }
    public boolean errorInCrcCheckerExists() { return crcChecker; }
    public void setId(UUID id) { this.id = id; }
    public void setErrorInRouting(boolean errorInRouting) { this.errorInRouting = errorInRouting; }
    public void setCrcChecker(boolean crcChecker) { this.crcChecker = crcChecker; }
}
