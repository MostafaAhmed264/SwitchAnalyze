package SwitchAnalyzer.Database;

import SwitchAnalyzer.Kafka.Producer;
import SwitchAnalyzer.Kafka.Topics;
import SwitchAnalyzer.Network.IP;
import SwitchAnalyzer.Sockets.JettyWebSocketServer;
import SwitchAnalyzer.miscellaneous.JSONConverter;
import com.datastax.driver.core.*;
import java.util.ArrayList;
import java.util.Map;

public class DBSelect {
    //session is used in order to execute the query
    private static final Session session = DBConnect.getSession();
    //selectedAttributes is string builder containing the attributes passed next to the word SELECT
    private static StringBuilder selectedAttributes;
    //whereCondition is string builder containing the conditions passed next to WHERE
    private static StringBuilder whereCondition;
    //fromTableName is string builder containing FROM table name
    private static StringBuilder fromTableName;
    private static String tableName;
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
        tableName = "runs";
        begin();
    }
    public static void beginSelectSwitches()
    {
        fromTableName = new StringBuilder("FROM switches");
        tableName = "switches";
        begin();
    }
    public static void beginSelectFrames(String runNo)
    {
        fromTableName = new StringBuilder("FROM frames_run").append(runNo);
        tableName = "frames";
        begin();
    }
    private static void begin()
    {
        if (JSON) {
            selectedAttributes = new StringBuilder("SELECT JSON ");
        } else {
            selectedAttributes = new StringBuilder("SELECT ");
        }
        if (selectAll) {
            if(tableName == "switches")
            {
                viewSwitchName();
                otherAttribute();
                viewTotalNoOfPorts();
            }
            else if (tableName == "runs")
            {
                viewRunNo();
                otherAttribute();
                viewRunDetails();
            }
            else
            {
                viewPort();
                otherAttribute();
                viewDirection();
                otherAttribute();
                viewFrameData();
                otherAttribute();
                viewCrcChecker();
                otherAttribute();
                viewErrorInRouting();
            }
        }
        whereCondition = new StringBuilder(" WHERE ");
    }
    /****************************** Switch conditions and view **************************************/
    public static void viewSwitchName(){ selectedAttributes.append("switchName "); }

    public static void viewTotalNoOfPorts(){ selectedAttributes.append("totalNoOfPorts "); }
    public static void conditionSwitchName(String switchName) {
        whereCondition.append("switchName = ").append(switchName);
    }
    public static void conditionTotalNoOfPorts(long totalNoOfPorts) {
        whereCondition.append("totalNoOfPorts = ").append(String.valueOf(totalNoOfPorts));
    }
    /****************************** Run conditions and view ***************************************/
    public static void viewRunNo() { selectedAttributes.append("runNo "); }
    public static void viewRunDetails() { selectedAttributes.append("runDetails "); }
    public static void conditionRunDetails(String headerName)
    {
        whereCondition.append("runDetails CONTAINS KEY '").append(headerName).append("'");
        ALLOWFILTERING = true;
    }
    public static void conditionRunNo(long runNo) {

        whereCondition.append("runNo = ").append(String.valueOf(runNo));
    }
    /*********************** Frame condition and view ******************************************/
    public static void viewPort() { selectedAttributes.append("port "); }
    public static void viewDirection() { selectedAttributes.append("direction "); }
    public static void viewFrameData() { selectedAttributes.append("frameDetails "); }
    public static void viewCrcChecker() { selectedAttributes.append("crcChecker "); }
    public static void viewErrorInRouting() { selectedAttributes.append("errorInRouting "); }
    public static void conditionID(long id) {
        whereCondition.append("ID = ").append(String.valueOf(id));
    }
    public static void conditionPort(int port) {
        whereCondition.append("port = ").append(String.valueOf(port));
    }
    public static void conditionDirection(String direction) {
        whereCondition.append("direction = ").append(direction);
    }
    public static void conditionFrameData(String headerName)
    {
        whereCondition.append("frameDetails CONTAINS KEY '").append(headerName).append("'");
        ALLOWFILTERING = true;
    }
    public static void conditionCrcChecker(boolean crcChecker) {
        whereCondition.append("crcChecker = ").append(String.valueOf(crcChecker));
    }
    public static void conditionErrorInRouting(boolean errorInRouting) {
        whereCondition.append("errorInRouting = ").append(String.valueOf(errorInRouting));
    }
    /*********************************Other condition or attribute ********************************/
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
    public static void executeSelect(String switchName,String runNo,String table,int size)
    {
        ResultSet rs = beginExecuteSelect();
        if(JSON)
        {
            selectJSON_frames_kafka(rs,switchName,runNo,table,size);
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
            String runNo = "" + run.getRunNo();
            Map<String, String> runDetails = run.rundetails;
            Map<String, String> additonal = run.additional;
            DBRun run_gui = new DBRun(runNo,runDetails,additonal);
            runs.add(run_gui);
        }
        return runs;
    }
    /**
     * Description :
     *          it iterates on the rows of resultSet and produce the frame in kafka
     */
    private static void selectJSON_frames_kafka(ResultSet rs,String switchName,String runNo,String table,int size)
    {
//        Producer dataProducer = new Producer(IP.ip1);
        for (Row row : rs)
        {
            String frame_json = row.getString("[json]");
            DBFrame frame = JSONConverter.fromJSON(frame_json, DBFrame.class);
            frame.switchName = switchName;
            frame.runNo = runNo;
            frame.table = table;
            frame.frameDetails = frame.framedetails;
            frame.Direction = frame.direction;
            frame.bytes = "Frame details";
            if(size == 1)
                frame.json = "history";
            else
                frame.json = "compare";
            JettyWebSocketServer.writeMessage(JSONConverter.toJSON(frame));
//            dataProducer.produce(JSONConverter.toJSON(new DBFrame(frame_json,runNo,switchName,table)), Topics.CompareRuns);
//            System.out.println("produced frame in kafka");
//            dataProducer.flush();
        }
    }
    /************************ selectAll based on specific frame data header **********************/
    public static String selectSpecificFrameDataHeader(String headerName)
    {
        setConditions_SpecificFrameDataHeader();
        beginSelectFrames(""+DBConnect.getLastRun());
        conditionFrameData(headerName);
        return executeSelect();
    }
    public static String selectSpecificFrameDataHeader(long runNo,String headerName)
    {
        setConditions_SpecificFrameDataHeader();
        beginSelectFrames(""+runNo);
        conditionFrameData(headerName);
        return executeSelect();
    }
    public static String selectSpecificFrameDataHeader(String switchName,long runNo,String headerName)
    {
        KeySpace.useKeyspace_Node(switchName);
        setConditions_SpecificFrameDataHeader();
        beginSelectFrames(""+runNo);
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
        return JSONConverter.toJSON(selectSwitches().dbSwitches);
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
            if (dbSwitches.get(i).getSwitchName().contains(" ")) {
                dbSwitches.get(i).setSwitchName(dbSwitches.get(i).getSwitchName().replace(" ", "_"));
            }
            KeySpace.useKeyspace_Node(dbSwitches.get(i).getSwitchName());
            setJSON(true);
            beginSelectRuns();
            try {
                dbSwitches.get(i).stats = executeSelect();
            }
            catch (Exception e)
            {
                dbSwitches.remove(i);
                i--;
            }
            if(dbSwitches.get(i).stats.isEmpty())
            {
                dbSwitches.remove(i);
                i--;
            }
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
            Long noOfPorts = row.getLong("totalNoOfPorts");
            String totalNoOfPorts;
            if(noOfPorts == 0)
                totalNoOfPorts = "Undefined";
            else
                totalNoOfPorts = "" + noOfPorts;
            DBSwitch dbswitch = new DBSwitch(switchName,totalNoOfPorts);
            switches.add(dbswitch);
        }
        return switches;
    }
    /************************************ Compare runs ******************************************/
    public static void compareRuns(ArrayList<Run_Gui> run_guis)
    {
        ArrayList<Thread> threads = new ArrayList<>();
        for (int i = 0; i < run_guis.size(); i++)
        {
            int finalI = i;
            Thread t = new Thread(() ->
                    showSpecificRun(run_guis.get(finalI).switchName,run_guis.get(finalI).runNo,"" + (finalI +1),run_guis.size()));
            t.start();
            threads.add(t);
        }
        for (Thread t : threads) {
            try { t.join(); }
            catch (Exception e) {}
        }
    }
    public static void showSpecificRun(String switchName, String runNo,String table,int size)
    {
        if (switchName.contains(" ")) {
            switchName = switchName.replace(" ", "_");
        }
        KeySpace.useKeyspace_Node(switchName);
        selectRunDetails(switchName, runNo,table,size);
    }
    private static void selectRunDetails(String switchName ,String runNo,String table,int size)
    {
        setSelectAll(true);
        setJSON(true);
        setCondition(false);
        setALLOWFILTERING(false);
        beginSelectFrames(runNo);
        executeSelect(switchName,runNo,table,size);
    }
}
