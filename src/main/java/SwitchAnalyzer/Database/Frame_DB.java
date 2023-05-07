package SwitchAnalyzer.Database;

import com.datastax.driver.core.utils.UUIDs;

import java.util.HashMap;
import java.util.UUID;

public class Frame_DB implements Frame
{
    private UUID id ;

    public String port = "1";
    public enum Direction {INBOUND,OUTBOUND}
    public Frame_DB.Direction directionEnum;
    public String Direction;
    public HashMap<String, String> frameDetails;
    private boolean errorInRouting;
    private boolean crcChecker;
    // this  variable is for producing in kafka when inserting a frame in the DB
    public String bytes;
    public Frame_DB() { id = UUIDs.timeBased(); }
    public UUID getID() { return id; }
    public boolean errorInRoutingExists() { return errorInRouting; }
    public boolean errorInCrcCheckerExists() { return crcChecker; }
    public void setErrorInRouting(boolean errorInRouting) { this.errorInRouting = errorInRouting; }
    public void setCrcChecker(boolean crcChecker) { this.crcChecker = crcChecker; }
}
