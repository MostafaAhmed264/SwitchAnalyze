package SwitchAnalyzer.Database;

import SwitchAnalyzer.miscellaneous.JSONConverter;
import com.datastax.driver.core.*;

import java.sql.Timestamp;
import java.util.ArrayList;

public class DBSelect {
    //session is used in order to execute the query
    private static final Session session = DBConnect.getSession();
    //selectedAttributes is string builder containing the attributes passed next to the word SELECT
    private static StringBuilder selectedAttributes;
    //private static List<String> attributesToBeShown;
    //whereCondition is string builder containing the conditions passed next to WHERE
    private static StringBuilder whereCondition;
    //fromTableName is string builder containing FROM table name
    private static StringBuilder fromTableName;
    //This is a boolean that will be set in order to tell me if the select has specific attributes or it is select all
    private static boolean selectAll;
    //wholeSelectQuery is the whole cql statement that will be executed
    private static StringBuilder wholeSelectQuery;
    private static String jsonConverter;
    private static boolean JSON;
    private static boolean condition;
    private static boolean ALLOWFILTERING;

    public static void setALLOWFILTERING(boolean ALLOWFILTERING) {
        DBSelect.ALLOWFILTERING = ALLOWFILTERING;
    }
    public static boolean isALLOWFILTERING() {
        return ALLOWFILTERING;
    }
    public static long getLastRun(){
        StringBuilder sb = new StringBuilder("SELECT MAX(runNo) FROM runs ;");
        final String query = sb.toString();
        ResultSet rs = session.execute(query);
        Row row = rs.one();
        long latestRun;
        // see what happens if there are no rows
        try{
            latestRun = (long)row.getObject(0);
        }
        catch(Exception e){
            return 0;
        }

        return latestRun;
    }

    public static boolean isSelectAll() {
        return selectAll;
    }
    public static <T> T selectAllNoConditionsTest(){
        DBSelect.setSelectAll(true);
        DBSelect.setJSON(true);
        DBSelect.setCondition(false);
        DBSelect.beginSelectFrames(1);
        return (T) DBSelect.executeSelect();
    }
    public static void setSelectAll(boolean selectAll) {
        DBSelect.selectAll = selectAll;
    }

    public static void setJSON(boolean JSON) {
        DBSelect.JSON = JSON;
    }

    public static void setCondition(boolean condition) {
        DBSelect.condition = condition;
    }

    public static boolean isThereCondition() {
        return condition;
    }

    public static boolean isJSON() {
        return JSON;
    }

    /*
    Input : condition string
    Output : void
    Description :
        This function will begin the select query with a condition all or specific
        all means we need to execute select all
        specific means we need to execute select certain attributes
    */
    public static void beginSelectRuns() {
        fromTableName = new StringBuilder("FROM runs");
        jsonConverter = "runs";
        if (selectAll) {
            if (JSON) {
                selectedAttributes = new StringBuilder("SELECT JSON * ");
            } else {
                selectedAttributes = new StringBuilder("SELECT * ");
            }
        } else {
            if (JSON) {
                selectedAttributes = new StringBuilder("SELECT JSON ");
            } else {
                selectedAttributes = new StringBuilder("SELECT ");
            }

        }
        whereCondition = new StringBuilder(" WHERE ");
    }

    public static void beginSelectHistory() {
        fromTableName = new StringBuilder("FROM switches");
        //TableName = "switches";
        if (selectAll) {
            if (JSON) {
                selectedAttributes = new StringBuilder("SELECT JSON * ");
            } else {
                selectedAttributes = new StringBuilder("SELECT * ");
            }
        } else {
            if (JSON) {
                selectedAttributes = new StringBuilder("SELECT JSON ");
            } else {
                selectedAttributes = new StringBuilder("SELECT ");
            }

        }
        whereCondition = new StringBuilder(" WHERE ");
    }

    public static void beginSelectFrames(long runNo) {
        fromTableName = new StringBuilder("FROM frames_run").append(runNo);
        jsonConverter = "frames";
        if (selectAll) {
            if (JSON) {
                selectedAttributes = new StringBuilder("SELECT JSON * ");
            } else {
                selectedAttributes = new StringBuilder("SELECT * ");
            }
        } else {
            if (JSON) {
                selectedAttributes = new StringBuilder("SELECT JSON ");
            } else {
                selectedAttributes = new StringBuilder("SELECT ");
            }

        }
        whereCondition = new StringBuilder(" WHERE ");
    }

    /****************************************************Run conditions and view*****************************************************************************/
    public static void conditionRunNo(long runNo) {
        whereCondition.append("RunNo = ").append(String.valueOf(runNo));
    }

    public static void viewRunNo() {
        selectedAttributes.append("RunNo ");
        //attributesToBeShown.add("RunNo");
    }

    public static void conditionStartTimeStamp(Timestamp startTimeStamp) {
        whereCondition.append("StartTimeStamp = ").append(startTimeStamp.toString());
    }

    public static void viewStartTimeStamp() {
        selectedAttributes.append("StartTimeStamp ");
        //attributesToBeShown.add("StartTimeStamp");
    }

    public static void conditionEndTimeStamp(Timestamp endTimeStamp) {
        whereCondition.append("EndTimeStamp = ").append(endTimeStamp.toString());

    }

    public static void viewEndTimeStamp() {
        selectedAttributes.append("EndTimeStamp ");
        //attributesToBeShown.add("EndTimeStamp");
    }

    public static void conditionSwitchName(String switchName) {
        whereCondition.append("SwitchName = ").append(switchName);

    }

    public static void viewSwitchName() {
        selectedAttributes.append("SwitchName ");
        //attributesToBeShown.add("SwitchName");
    }

    public static void conditionPacketLoss(float packetLoss) {
        whereCondition.append("PacketLoss = ").append(String.valueOf(packetLoss));

    }

    public static void viewPacketLoss() {
        selectedAttributes.append("PacketLoss ");
        //attributesToBeShown.add("PacketLoss");
    }

    public static void conditionLatency(float latency) {
        whereCondition.append("Latency = ").append(String.valueOf(latency));

    }

    public static void viewLatency() {
        selectedAttributes.append("Latency ");
        //attributesToBeShown.add("Latency");
    }

    public static void conditionThroughput(float throughput) {
        whereCondition.append("Throughput = ").append(String.valueOf(throughput));

    }

    public static void viewThroughput() {
        selectedAttributes.append("Throughput ");
        //attributesToBeShown.add("Throughput");
    }

    public static void conditionSuccessfulFramesPercentage(float successfulFramesPercentage) {
        whereCondition.append("SuccessfulFramesPercentage = ").append(String.valueOf(successfulFramesPercentage));

    }

    public static void viewSuccessfulFramesPercentage() {
        selectedAttributes.append("SuccessfulFramesPercentage ");
        //attributesToBeShown.add("SuccessfulFramesPercentage");
    }

    public static void conditionFramesWithErrorsPercentage(float framesWithErrorsPercentage) {
        whereCondition.append("FramesWithErrorsPercentage = ").append(String.valueOf(framesWithErrorsPercentage));

    }

    public static void viewFramesWithErrorsPercentage() {
        selectedAttributes.append("FramesWithErrorsPercentage ");
        //attributesToBeShown.add("FramesWithErrorsPercentage");
    }


    /******************************************************Frame condition and view**********************************************************************/
    public static void conditionID(long id) {
        whereCondition.append("ID = ").append(String.valueOf(id));

    }

    public static void viewID() {
        selectedAttributes.append("ID ");
        //attributesToBeShown.add("ID");
    }

    public static void conditionTimestamp(Timestamp timestamp) {
        whereCondition.append("Timestamp = ").append(timestamp.toString());

    }

    public static void viewTimestamp() {
        selectedAttributes.append("Timestamp ");
        //attributesToBeShown.add("Timestamp");
    }

    public static void conditionSendingPort(int sendingPort) {
        whereCondition.append("SendingPort = ").append(String.valueOf(sendingPort));

    }

    public static void viewSendingPort() {
        selectedAttributes.append("SendingPort ");
        //attributesToBeShown.add("SendingPort");
    }

    public static void conditionRecievingPort(int recievingPort) {
        whereCondition.append("RecievingPort = ").append(String.valueOf(recievingPort));
    }

    public static void viewRecievingPort() {
        selectedAttributes.append("RecievingPort ");
        //attributesToBeShown.add("RecievingPort");
    }

    public static void conditionFrameData(String headerName)
    {
        whereCondition.append("frameData CONTAINS KEY '").append(headerName).append("'");
        ALLOWFILTERING = true;
    }

    public static void viewFrameData() {
        selectedAttributes.append("frameData ");
        //attributesToBeShown.add("frameData");
    }

    public static void conditionErrorInRouting(boolean errorInRouting) {
        whereCondition.append("ErrorInRouting = ").append(String.valueOf(errorInRouting));
    }

    public static void viewErrorInRouting() {
        selectedAttributes.append("ErrorInRouting ");
        //attributesToBeShown.add("ErrorInRouting");
    }

    public static void conditionCrcChecker(boolean crcChecker) {
        whereCondition.append("CrcChecker = ").append(String.valueOf(crcChecker));
    }

    public static void viewCrcChecker() {
        selectedAttributes.append("CrcChecker ");
        //attributesToBeShown.add("CrcChecker");
    }

    /**
     * Input : void
     * Output : void
     * Description :
     * If select is select all the function will add the word AND in the condition
     * If select is selected specific the function will add the condition and the attribute
     */
    public static void otherCondition() {
        whereCondition.append(" AND ");
    }

    /**
     * Input : void
     * Output : void
     * Description :
     * If select is select all the function will add the word AND in the condition
     * If select is selected specific the function will add the condition and the attribute
     */
    public static void otherAttribute() {
        selectedAttributes.append(", ");
    }

    public static <T> T executeSelect()
    {
        wholeSelectQuery = new StringBuilder();
        wholeSelectQuery.append(selectedAttributes).append(fromTableName);
        if(isThereCondition())
        {
            wholeSelectQuery.append(whereCondition);
            if(isALLOWFILTERING())
                wholeSelectQuery.append(" ALLOW FILTERING");
        }
        wholeSelectQuery.append(";");
        final String query = wholeSelectQuery.toString();
        System.out.println(query);
        SimpleStatement x = new SimpleStatement(query);
        x.setConsistencyLevel(ConsistencyLevel.ONE);
        ResultSet rs = session.execute(x);
        if(isJSON())
        {
            return (T) selectJSON(rs);
        }
        else
        {
            return (T) rs;
        }
    }
    public static <T> T selectJSON(ResultSet rs)
    {
        if(jsonConverter == "runs")
        {
            return (T) selectJSON_runs(rs);
        }
        return (T) selectJSON_frames(rs);
    }
    private static ArrayList<DBRun> selectJSON_runs(ResultSet rs)
    {
        ArrayList<DBRun> runs=new ArrayList<>();
        for (Row row : rs)
        {
            String jsonString = row.getString("[json]");
            DBRun run = JSONConverter.fromJSON(jsonString,DBRun.class);
            runs.add(run);
        }
        return runs;
    }
    private static ArrayList<DBFrame> selectJSON_frames(ResultSet rs)
    {
        ArrayList<DBFrame> frames=new ArrayList<>();
        for (Row row : rs)
        {
            String jsonString = row.getString("[json]");
            DBFrame frame = JSONConverter.fromJSON(jsonString,DBFrame.class);
            frames.add(frame);
        }
        return frames;
    }
    public static String selectAllJson_LastRunTest()
    {
        setSelectAll(true);
        setJSON(true);
        beginSelectFrames(DBConnect.getLastRun());
        String jsonResult = executeSelect();
        return jsonResult;
    }
    public static String selectAllJson_LastRun_SpecificHeaderTest(String headerName)
    {
        setSelectAll(true);
        setJSON(true);
        setCondition(true);
        setALLOWFILTERING(false);
        beginSelectFrames(DBConnect.getLastRun());
        conditionFrameData(headerName);
        String jsonResult = executeSelect();
        return jsonResult;
    }
    private static ArrayList<DBSwitch> convertResultSetHistory(ResultSet historyResult)
    {
        ArrayList<DBSwitch> switches = new ArrayList<>();
        for (Row row : historyResult)
        {
            String switchName = row.getString("switchName");
            long totalNoOfPorts = row.getLong("totalNoOfPorts");
            DBSwitch dbswitch = new DBSwitch(switchName,totalNoOfPorts);
            switches.add(dbswitch);
        }
        return switches;
    }
    private static DBSwitches selectSwitches()
    {
        setSelectAll(true);
        setJSON(false);
        setCondition(false);
        setALLOWFILTERING(false);
        beginSelectHistory();
        ResultSet historyResult = executeSelect();
        ArrayList<DBSwitch> dbSwitches = convertResultSetHistory(historyResult);
        for (int i = 0; i < dbSwitches.size(); i++)
        {
            KeySpace.useKeyspace_Node(dbSwitches.get(i).getSwitchName());
            setJSON(true);
            beginSelectRuns();
            dbSwitches.get(i).setSwitchRuns(executeSelect());
        }
        return new DBSwitches(dbSwitches);
    }
    public static String showHistory()
    {
        KeySpace.useKeyspace_Node("history");
        return JSONConverter.toJSON(selectSwitches());
    }
}
