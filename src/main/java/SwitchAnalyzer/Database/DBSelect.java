package SwitchAnalyzer.Database;

import SwitchAnalyzer.Kafka.Producer;
import SwitchAnalyzer.Kafka.Topics;
import SwitchAnalyzer.Network.IP;
import SwitchAnalyzer.miscellaneous.JSONConverter;
import com.datastax.driver.core.*;
import java.util.ArrayList;

public class DBSelect {
    //session is used in order to execute the query
    private static final Session session = DBConnect.getSession();
    //selectedAttributes is string builder containing the attributes passed next to the word SELECT
    private static StringBuilder selectedAttributes;
    //whereCondition is string builder containing the conditions passed next to WHERE
    private static StringBuilder whereCondition;
    //fromTableName is string builder containing FROM table name
    private static StringBuilder fromTableName;
    //wholeSelectQuery is the whole cql statement that will be executed
    private static StringBuilder wholeSelectQuery;
    private static boolean selectAll;
    private static boolean JSON;
    private static boolean condition;
    private static boolean ALLOWFILTERING;

    public static void setALLOWFILTERING(boolean ALLOWFILTERING) {
        DBSelect.ALLOWFILTERING = ALLOWFILTERING;
    }
    public static void setSelectAll(boolean selectAll) {
        DBSelect.selectAll = selectAll;
    }
    public static void setJSON(boolean JSON) {
        DBSelect.JSON = JSON;
    }
    public static void setCondition(boolean condition) {DBSelect.condition = condition;}
    /*********************get last runNo in the current switch**********/
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
    /***********************************Begin select**************************************/
    public static void beginSelectRuns()
    {
        fromTableName = new StringBuilder("FROM runs");
        begin();
    }
    public static void beginSelectSwitches()
    {
        fromTableName = new StringBuilder("FROM switches");
        begin();
    }
    public static void beginSelectFrames(long runNo)
    {
        fromTableName = new StringBuilder("FROM frames_run").append(runNo);
        begin();
    }
    private static void begin()
    {
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
    /******************************Run conditions and view*******************************8*******/
    public static void conditionRunNo(long runNo) {
        whereCondition.append("runNo = ").append(String.valueOf(runNo));
    }
    public static void viewRunNo() { selectedAttributes.append("runNo "); }
    public static void conditionRunDetails(String headerName)
    {
        whereCondition.append("runDetails CONTAINS KEY '").append(headerName).append("'");
        ALLOWFILTERING = true;
    }
    public static void viewRunDetails() { selectedAttributes.append("runDetails "); }
    /*********************** Frame condition and view ******************************************/
    public static void conditionID(long id) {
        whereCondition.append("ID = ").append(String.valueOf(id));
    }
    public static void viewID() { selectedAttributes.append("ID "); }
    public static void conditionPort(int port) {
        whereCondition.append("port = ").append(String.valueOf(port));
    }
    public static void viewPort() { selectedAttributes.append("port "); }
    public static void conditionDirection(String direction) {
        whereCondition.append("direction = ").append(direction);
    }
    public static void viewDirection() { selectedAttributes.append("direction "); }

    public static void conditionFrameData(String headerName)
    {
        whereCondition.append("frameData CONTAINS KEY '").append(headerName).append("'");
        ALLOWFILTERING = true;
    }
    public static void viewFrameData() { selectedAttributes.append("frameData "); }

    public static void conditionErrorInRouting(boolean errorInRouting) {
        whereCondition.append("errorInRouting = ").append(String.valueOf(errorInRouting));
    }
    public static void viewErrorInRouting() { selectedAttributes.append("errorInRouting "); }
    public static void conditionCrcChecker(boolean crcChecker) {
        whereCondition.append("crcChecker = ").append(String.valueOf(crcChecker));
    }
    public static void viewCrcChecker() { selectedAttributes.append("crcChecker "); }
    /**
     * Input : void
     * Output : void
     * Description :
     * If select is select all the function will add the word AND in the condition
     * If select is selected specific the function will add the condition and the attribute
     */
    public static void otherCondition() { whereCondition.append(" AND "); }
    /**
     * Input : void
     * Output : void
     * Description :
     * If select is select all the function will add the word AND in the condition
     * If select is selected specific the function will add the condition and the attribute
     */
    public static void otherAttribute() { selectedAttributes.append(", "); }
    /************************************* Execute select ***************************************/
    private static ResultSet beginExecuteSelect()
    {
        wholeSelectQuery = new StringBuilder();
        wholeSelectQuery.append(selectedAttributes).append(fromTableName);
        if(condition)
        {
            wholeSelectQuery.append(whereCondition);
            if(ALLOWFILTERING)
                wholeSelectQuery.append(" ALLOW FILTERING");
        }
        wholeSelectQuery.append(";");
        final String query = wholeSelectQuery.toString();
        System.out.println(query);
        SimpleStatement x = new SimpleStatement(query);
        x.setConsistencyLevel(ConsistencyLevel.ONE);
        return session.execute(x);
    }
    /**
     * Description :
     *          build the string builder called wholeSelectQuery depending on different scenarios
     *          and return a type accordingly (arraylist of runs or resultSet)
     */
    public static <T> T executeSelect()
    {
        ResultSet rs = beginExecuteSelect();
        if(JSON)
        {
            return (T) selectJSON_runs(rs);
        }
        else
        {
            return (T) rs;
        }
    }
    /**
     * Description :
     *          build the string builder called wholeSelectQuery depending on different scenarios
     *          and set attributes for DBFrame and produce the frame in kafka
     */
    public static void executeSelect(String switchName,long runNo)
    {
        ResultSet rs = beginExecuteSelect();
        if(JSON)
        {
            selectJSON_frames_kafka(rs,switchName,runNo);
        }
        else
        {
            System.out.println("the select is not json");
        }
    }
    /**
     * Description :
     *          it iterates on the rows of resultSet and convert the result into DBRun object and
     *          returns arraylist of DBRun
     */
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
    /**
     * Description :
     *          it iterates on the rows of resultSet and produce the frame in kafka
     */
    private static void selectJSON_frames_kafka(ResultSet rs,String switchName,long runNo)
    {
        Producer dataProducer = new Producer(IP.ip1);
        for (Row row : rs)
        {
            String frame_json = row.getString("[json]");
            long runno = runNo;
            String switchname = switchName;
            dataProducer.produce(JSONConverter.toJSON(new DBFrame(frame_json,runNo,switchName)), Topics.ProcessedFramesFromHPC);
            System.out.println("produced frame in kafka");
            dataProducer.flush();
        }
    }
    /************************ selectAll based on specific frame data header **********************/
    public static String selectSpecificFrameDataHeader(String headerName)
    {
        setConditions_SpecificFrameDataHeader();
        beginSelectFrames(DBConnect.getLastRun());
        conditionFrameData(headerName);
        return executeSelect();
    }
    public static String selectSpecificFrameDataHeader(long runNo,String headerName)
    {
        setConditions_SpecificFrameDataHeader();
        beginSelectFrames(runNo);
        conditionFrameData(headerName);
        return executeSelect();
    }
    public static String selectSpecificFrameDataHeader(String switchName,long runNo,String headerName)
    {
        KeySpace.useKeyspace_Node(switchName);
        setConditions_SpecificFrameDataHeader();
        beginSelectFrames(runNo);
        conditionFrameData(headerName);
        return executeSelect();
    }
    private static void setConditions_SpecificFrameDataHeader()
    {
        setSelectAll(true);
        setJSON(true);
        setCondition(true);
        setALLOWFILTERING(false);
    }

    /************************************ Show history ******************************************/
    /**
     * Description :
     *       it goes into history keyspace and retrieve all the details about all switches in it
     */
    public static String showHistory()
    {
        KeySpace.useKeyspace_Node("history");
        return JSONConverter.toJSON(selectSwitches());
    }
    private static DBSwitches selectSwitches()
    {
        setSelectAll(true);
        setJSON(false);
        setCondition(false);
        setALLOWFILTERING(false);
        beginSelectSwitches();
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
    /**
     * Description :
     *          it iterates on the rows of resultSet and handles the attributes of DBSwitch by
     *          setting them and returns arraylist of DBSwitch
     */
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
    /************************************ Compare runs ******************************************/
    public static void compareRuns(ArrayList<Run_Gui> run_guis)
    {
        for (int i = 0; i < run_guis.size(); i++)
        {
            showSpecificRun(run_guis.get(i).switchName,run_guis.get(i).runNo);
        }
    }
    public static void showSpecificRun(String switchName, long runNo)
    {
        KeySpace.useKeyspace_Node(switchName);
        selectRunDetails(switchName, runNo);
    }
    private static void selectRunDetails(String switchName ,long runNo)
    {
        setSelectAll(true);
        setJSON(true);
        setCondition(false);
        setALLOWFILTERING(false);
        beginSelectFrames(runNo);
        executeSelect(switchName,runNo);
    }
}
