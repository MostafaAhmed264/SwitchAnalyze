package SwitchAnalyzer.Database;

import com.datastax.driver.core.*;
import com.datastax.driver.core.utils.UUIDs;

import java.util.ArrayList;
import java.util.UUID;

public class DBInsert
{
    private static String keyspaceName;
    private static PreparedStatement preparedStatement;
    public static String getKeyspaceName() {
        return keyspaceName;
    }

    public static void setKeyspaceName(String keyspaceName) {
        DBInsert.keyspaceName = keyspaceName;
    }

    public static PreparedStatement getPreparedStatement() {
        return preparedStatement;
    }

    public static void setPreparedStatement(PreparedStatement preparedStatement) {
        DBInsert.preparedStatement = preparedStatement;
    }

    /**
     * Input : the number of the current run
     * Output : void
     * Description :
     * The function makes a prepared statement so, we can enhance insert querie and decrease time
     * and impact on performance
     * This function is made for the tables of frames_run
     */
    public static void preparedStatement(long runNo) {
        StringBuilder sb = new StringBuilder("INSERT INTO frames_run")
                .append(runNo)
                .append("(id,timeStamp,fullPacket,sendingPort,recievingPort,networkHeaderName,networkHeaderBytes,transportHeaderName,transportHeaderBytes,errorInRouting,crcChecker) ")
                .append("VALUES (?,?,?,?,?,?,?,?,?,?,?);");
        setPreparedStatement(DBConnect.getSession().prepare(sb.toString()));
    }
    /**
     * Input : Frame
     * Output : void
     * Description :
     * The function insert a frame in the Frames of the current run table.
     */
    public static void  insert(DBFrame frame) {
        UUID timeUuid = UUIDs.timeBased();
        BoundStatement boundStatement = preparedStatement.bind(
                timeUuid,
                frame.getSendingPort(),
                frame.getRecievingPort(),
                frame.errorInRoutingExists(),
                frame.errorInCrcCheckerExists());
        DBConnect.getSession().execute(boundStatement);
    }
    public static void insertFrameJson(String frameJson)
    {
        StringBuilder sb = new StringBuilder(
                "INSERT INTO frames_run"+DBConnect.getLastRun()+" JSON '"+frameJson+"';");
    }
    /**
     * Input : run
     * Output : void
     * Description :
     * The function insert a run in the Runs table of the current switch .
     */
    public static void insert(DBRun run)
    {
        UUID timeUuid = UUIDs.timeBased();
        StringBuilder sb = new StringBuilder("INSERT INTO frames_run")
                .append(DBConnect.getLastRun())
                .append("(runNo,startTimeStamp,endTimeStamp,packetLoss,latency,throughput,successfulFramesPercentage,framesWithErrorsPercentage) ")
                .append("VALUES (")
                .append(String.valueOf(run.getRunNo()))
                .append(", ")
                .append(run.getStartTimeStamp().toString())
                .append(", ")
                .append(run.getEndTimeStamp().toString())
                .append(", ")
                .append(String.valueOf(run.getPacketLoss()))
                .append(", ")
                .append(String.valueOf(run.getLatency()))
                .append(", ")
                .append(run.getThroughput())
                .append(", ")
                .append(run.getSuccessfulFramesPercentage())
                .append(", ")
                .append(String.valueOf(run.getFramesWithErrorsPercentage()))
                .append(");");
        final String query = sb.toString();
        DBConnect.getSession().execute(query);
    }
    /**
     * Input : switch
     * Output : void
     * Description :
     * The function will check if the switch already exists in the table switches
     * If it already exists then it will not insert it
     * else it will insert a row including the name of switch and the total number of ports
     */
    public static void insertSwitch(DBSwitch dbSwitch)
    {
        if(!isSwitchNameAlreadyExists(dbSwitch.getSwitchName()))
        {
            UUID timeUuid = UUIDs.timeBased();
            StringBuilder sb = new StringBuilder("INSERT INTO ").append("switches").append("(id,switchName,totalNoOfPorts) ").append("VALUES (").append(timeUuid.toString()).append(", '").append(dbSwitch.getSwitchName()).append("', ").append(String.valueOf(dbSwitch.getTotalNoOfPorts())).append(");");
            final String query = sb.toString();
            DBConnect.getSession().execute(query);
        }
    }
    /**
     * Input : name of switch
     * Output : boolean
     * Description :
     * The function will check if the switch already exists in the table switches
     * If it already exists then it will return true
     * else it will return false
     */
    private static boolean isSwitchNameAlreadyExists(String switchName)
    {
        StringBuilder sb = new StringBuilder("SELECT * FROM switches WHERE switchName = '").append(switchName).append("'ALLOW FILTERING;");
        final String query = sb.toString();
        ResultSet rs = DBConnect.getSession().execute(query);
        if(rs.isExhausted())
        {
            return false;
        }
        return true;
    }

}
