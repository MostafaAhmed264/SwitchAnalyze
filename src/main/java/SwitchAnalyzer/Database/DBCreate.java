package SwitchAnalyzer.Database;

import com.datastax.driver.core.KeyspaceMetadata;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.UserType;
import org.apache.kafka.common.protocol.types.Field;


public class DBCreate {
    private static boolean switchesTableCreated = false;
    /**
     * Input : name of the table , name of the keyspace we are currently in
     * Output : boolean
     * Description :
     * This function will check if the table already exists in the current keyspace or not
     * If yes it will return true else it will return false
     */
    private static boolean IsTableExists(String tableName, String keyspaceName)
    {
        KeyspaceMetadata keyspace = DBConnect.getMetadata().getKeyspace(keyspaceName);

        // Check if the table exists
        if (keyspace.getTable(tableName) != null) {
            System.out.println("Table " + tableName + " exists in keyspace " + keyspaceName);
            return true;
        } else {
            System.out.println("Table " + tableName + " does not exist in keyspace " + keyspaceName);
        }
        return false;
    }
    /**
     * input : name of the keyspace we are currently in
     * output : void
     * Description :
     * This function will check if the table runs is already created
     * If no then it will create the table Runs that will have details about the runs done on a switch
     * Each switch will have its runs table
     * This function will be called when the master of masters connects at the beginning of a run
     */
    public static void createRunsTable(String keyspaceName)
    {
        if(!IsTableExists("runs",keyspaceName)){
            StringBuilder sb = new StringBuilder("CREATE TABLE IF NOT" +
                    " EXISTS runs")
                    .append("(")
                    .append("runNo bigint,")
                    .append("startTimeStamp timestamp,")
                    .append("endTimeStamp timestamp,")
                    .append("packetLoss float,")
                    .append("latency float,")
                    .append("throughput float,")
                    .append("successfulFramesPercentage float,")
                    .append("framesWithErrorsPercentage float,")
                    .append("PRIMARY KEY(runNo)" )
                    .append(");");
            final String query = sb.toString();
            DBConnect.getSession().execute(query);
        }
    }
    /**
     * input : the number of the run
     * output : void
     * Description :
     * This function creates the table frames for a run that will have details about each frame to be inserted
     * Each switch will have more than a table each represent the frames in one run
     * This function will be called when the master of masters connects at the beginning of a run
     */
    public static void  createFrames_RunTable(long runNO)
    {
        StringBuilder sb = new StringBuilder("CREATE TABLE IF NOT" +
                " EXISTS frames_run");
                sb.append(runNO)
                .append("(")
                .append("id timeuuid,")
                .append("timeStamp timestamp,")
                .append("sendingPort int,")
                .append("recievingPort int,")
                .append("header1name text,")
                .append("header2name text,")
                .append("header3name text,")
                .append("header4name text,")
                .append("header1data text,")
                .append("header2data text,")
                .append("header3data text,")
                .append("header4data text,")
                .append("errorInRouting boolean,")
                .append("crcChecker boolean,")
                .append("PRIMARY KEY(id)" )
                .append(");");
        final String query = sb.toString();
        DBConnect.getSession().execute(query);
    }
    /**
     * input : name of the keyspace we are currently in
     * output : void
     * Description :
     * This function will check if the boolean switchesTableCreated is false
     * If it is false then it will check if table switch is already created
     * If no then it creates the table switches that will have the name of the switch and the total number of ports
     * This function will be called when the master of masters connects at the beginning of a run
     */
    public static void createSwitchesTable(String keyspaceName)
    {
        if(switchesTableCreated == false)
        {
            if(!IsTableExists("switches",keyspaceName))
            {
                switchesTableCreated = true;
                StringBuilder sb = new StringBuilder("CREATE TABLE IF NOT" +
                        " EXISTS switches(");
                sb.append("id timeuuid,")
                        .append("switchName text,")
                        .append("totalNoOfPorts bigint,")
                        .append("PRIMARY KEY(id));");
                final String query = sb.toString();
                DBConnect.getSession().execute(query);
            }
        }
    }
    /**
     * Input : name of keyspace we are currently in
     * Output : void
     * Description :
     * This function will create UDT for each type of network header and transport header
     * but before creating it will check if the UDT is already created before in this keyspace
     */
    public static void createHeaders(String keyspaceName)
    {
        createNetworkHeaders(keyspaceName);
        createTransportHeaders(keyspaceName);
    }
    /**
     * Input : name of keyspace we are currently in
     * Output : void
     * Description :
     * This function will create UDT for each type of network header
     * but before creating it will check if the UDT is already created before in this keyspace
     */
    private static void createNetworkHeaders(String keyspaceName)
    {
        createIpv4Header(keyspaceName);
        createIpv6Header(keyspaceName);
    }
    /**
     * Input : name of keyspace we are currently in
     * Output : void
     * Description :
     * This function will create UDT for each type of transport header
     * but before creating it will check if the UDT is already created before in this keyspace
     */
    private static void createTransportHeaders(String keyspaceName)
    {
        createTcpHeader(keyspaceName);
        createUdpHeader(keyspaceName);
    }
    /**
     * Input : name of keyspace we are currently in
     * Output : void
     * Description :
     * This function will create UDT for Ipv4 network header
     * but before creating it will check if the UDT is already created before in this keyspace
     */
    private static void createIpv4Header(String keyspaceName)
    {
        if(!IsUDTExists(keyspaceName,"ipv4"))
        {
            StringBuilder sb = new StringBuilder("CREATE TYPE IF NOT" +
                    " EXISTS ipv4Header(");
            sb.append("versionNo int,")
                    .append("ipHeaderLength text,")
                    .append("TOS text,")
                    .append("totalLengthInBytes int,")
                    .append("ID int,")
                    .append("reservedFlag boolean,")
                    .append("dontFragmentFlag boolean,")
                    .append("moreFragmentFlag boolean,")
                    .append("fragmentOffset int,")
                    .append("timeToLive int,")
                    .append("upperLayerProtocol text,")
                    .append("headerChecksum text,")
                    .append("sourceIpAddress text,")
                    .append("destinationIpAddress text,")
                    .append(");");
            final String query = sb.toString();
            DBConnect.getSession().execute(query);
        }
    }
    /**
     * Input : name of keyspace we are currently in
     * Output : void
     * Description :
     * This function will create UDT for Ipv6 network header
     * but before creating it will check if the UDT is already created before in this keyspace
     */
    private static void createIpv6Header(String keyspaceName)
    {
        if(!IsUDTExists(keyspaceName,"ipv6Header"))
        {
            StringBuilder sb = new StringBuilder("CREATE TYPE IF NOT" +
                    " EXISTS ipv6Header(");
            sb.append("versionNo int,")
                    .append("trafficClass int,")
                    .append("flowLabel int,")
                    .append("payloadLength int,")
                    .append("nextHeader int,")
                    .append("hopLimit int,")
                    .append("srcMacAddress text,")
                    .append("dstMacAddress text,")
                    .append(");");
            final String query = sb.toString();
            DBConnect.getSession().execute(query);
        }
    }
    /**
     * Input : name of keyspace we are currently in
     * Output : void
     * Description :
     * This function will create UDT for Tcp transport header
     * but before creating it will check if the UDT is already created before in this keyspace
     */
    private static void createTcpHeader(String keyspaceName)
    {
        if(!IsUDTExists(keyspaceName,"tcpHeader"))
        {
            StringBuilder sb = new StringBuilder("CREATE TYPE IF NOT" +
                    " EXISTS tcpHeader(");
            sb.append("sourcePort int,")
                    .append("destinationPort text,")
                    .append("sequenceNumber int,")
                    .append("acknowledgementNumber int,")
                    .append("dataOffset int,")
                    .append("reserved int,")
                    .append("urgentFlag boolean,")
                    .append("acknowledgmentFlag boolean,")
                    .append("pushFlag boolean,")
                    .append("resetFlag boolean,")
                    .append("synchronizeFlag boolean,")
                    .append("finishFlag boolean,")
                    .append("window int,")
                    .append("checksum text,")
                    .append("urgentPointer int,")
                    .append(");");
            final String query = sb.toString();
            DBConnect.getSession().execute(query);
        }
    }
    /**
     * Input : name of keyspace we are currently in
     * Output : void
     * Description :
     * This function will create UDT for Udp transport header
     * but before creating it will check if the UDT is already created before in this keyspace
     */
    private static void createUdpHeader(String keyspaceName)
    {
        if(!IsUDTExists(keyspaceName,"udpHeader"))
        {
            StringBuilder sb = new StringBuilder("CREATE TYPE IF NOT" +
                    " EXISTS udpHeader(");
            sb.append("sourcePortNo int,")
                    .append("destinationPortNo int,")
                    .append("lengthInBytes int,")
                    .append("checksum int,")
                    .append(");");
            final String query = sb.toString();
            DBConnect.getSession().execute(query);
        }
    }
    /**
     * Input : name of keyspace we are currently in , name of the header we want to check
     * Output : void
     * Description :
     * This function will check if there is a UDT for this header already created before in this keyspace
     */
    private static boolean IsUDTExists(String keyspaceName , String headerName)
    {
        // Get the keyspace metadata
        KeyspaceMetadata keyspaceMetadata = DBConnect.getMetadata().getKeyspace(keyspaceName);
        // Check if the keyspace exists
        if (keyspaceMetadata != null) {
            // Get the user-defined type metadata
            UserType udtMetadata = keyspaceMetadata.getUserType(headerName);
            // Check if the user-defined type exists
            if (udtMetadata != null) {
                System.out.println("UDT " + headerName + " exists in keyspace " + keyspaceName);
                return true;
            } else {
                System.out.println("UDT " + headerName + " does not exist in keyspace " + keyspaceName);
            }
        } else {
            System.out.println("Keyspace " + keyspaceName + " does not exist");
        }
        return false;
    }
}
