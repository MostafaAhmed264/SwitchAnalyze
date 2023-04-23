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
    private int sendingPort;
    private int recievingPort;
    public HashMap<String, String> frameData;
    private boolean errorInRouting;
    private boolean crcChecker;
    public byte[] payload;
    public DBFrame() { id = UUIDs.timeBased(); }
    public UUID getID() { return id; }
    public int getSendingPort() { return sendingPort; }
    public int getRecievingPort() { return recievingPort; }
    public boolean errorInRoutingExists() { return errorInRouting; }
    public boolean errorInCrcCheckerExists() { return crcChecker; }
    public void setId(UUID id) { this.id = id; }
    public void setSendingPort(int sendingPort) { this.sendingPort = sendingPort; }
    public void setRecievingPort(int recievingPort) { this.recievingPort = recievingPort; }
    public void setErrorInRouting(boolean errorInRouting) { this.errorInRouting = errorInRouting; }
    public void setCrcChecker(boolean crcChecker) { this.crcChecker = crcChecker; }
    @Override
    public String toString() {
        return "DBFrame{" +
                "id=" +
                "\n, sendingPort=" + sendingPort +
                "\n, recievingPort=" + recievingPort +
                "\n, errorInRouting=" + errorInRouting +
                "\n, crcChecker=" + crcChecker +
                '}';
    }
}
