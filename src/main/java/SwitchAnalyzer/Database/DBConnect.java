package SwitchAnalyzer.Database;
import SwitchAnalyzer.Network.IP;
import com.datastax.driver.core.Metadata;
import com.datastax.driver.core.Session;
import org.apache.log4j.BasicConfigurator;

/*
*these IPS for DataBase Nodes
* 192.168.1.60
* 192.168.1.70
*
*
*
* */
public class DBConnect {

    /**
     * This object will be used when a device want to connect to a cassandra node
     */
    private static CassandraConnector connector;
    /**
     * This object will be used in queries
     */
    private static  Session session;
    /**
     * This object will be used for checking if keyspace , table or udt already exists
     */
    private static Metadata metadata;
    /**
     * This variable will tell about the last run we ran on the switch
     */
    private static long lastRun;

    public static long getLastRun() {
        return lastRun;
    }

    public static Session getSession() {
        return session;
    }

    public static Metadata getMetadata() {
        return metadata;
    }

    public static void setMetadata(Metadata metadata) {
        DBConnect.metadata = metadata;
    }

    /**
     * input : switch name and total number of ports
     * output : void
     * description :
     * This function is called by the master of masters at the beginning of the run
     * 1. It connects to a cassandra node
     * 2. It uses the keyspace history if it is already created or it will create it and then use it
     * 3. It creates the table switches if not already created
     * 4. It inserts a new row containing the name of the switch passed and its total number of ports if the name of switch not already there
     * 5. It uses the keyspace of this switch if it is already created or it will create it and then use it
     * 6. It creates the table runs if not already created
     * 7. It calls for the getLastRun to get the number of the last run made in this switch
     * 9. It creates a table for frames_Run that is specific to the new run that will begin now
     */
    public static void startRun(Switch_DB dbSwitch)
    {
        KeySpace.useKeyspace_MOM("history");
        DBCreate.createSwitchesTable("history");
        DBInsert.insertSwitch(dbSwitch);
        KeySpace.useKeyspace_MOM(dbSwitch.switchName);
        DBCreate.createRunsTable(dbSwitch.switchName);
        lastRun = DBSelect.getLastRun()+1;
        DBCreate.createFrames_RunTable(lastRun);
    }
    /**
     * input : switch name
     * output : void
     * description :
     * This function is called by any node of the HPC at any time while the run is still ongoing
     * 1. It connects to a cassandra node then it uses the keyspace of the switch
     * 2. It calls for the getLastRun to get the number of the last run made in this switch but it is not
     *    the number of the run that the master of masters created at the beginning of the run so we incremented the
     *    result by 1 to represent the right run
     */
    public static void connectToDB_Node(String switchName)
    {
        DBConnect.connect();
        KeySpace.useKeyspace_Node(switchName);
        lastRun = DBSelect.getLastRun()+1;
        DBInsert.setKeyspaceName(switchName);
    }
    /**
     * input : void
     * output : void
     * description :
     * This function is called when any node of the HPC or the MOM wants to connect
     * 1. It calls for connect function and pass to it list of ips
     *    ,number of port we want to connect to (if null then we will use the default "9042")
     *    ,metadata object to use it later for checking if keyspace , table or udt already exists
     * 2. It gets a session for the connector it connected to in order to be able to use that session later in queries
     * 3. It creates keyspacerepo to be used in managing keyspaces
     */
    public static void connect() {
        try {
            BasicConfigurator.configure();
            connector = new CassandraConnector();
            /* choose the node or nodes to connect with */
            connector.connect(IP.DBIps,null,metadata);
            session = connector.getSession();
            System.out.println("connected");
        } catch (Exception e) {
            System.out.println("Cant connect to DB server ");
            e.printStackTrace();
        }
    }
    /**
     * input : void
     * output : void
     * description :
     * This function is close the connection with the database
     */
    public static void closeConnectionToDB() {
        connector.close();
    }

}
