package SwitchAnalyzer.Database;

import com.datastax.driver.core.KeyspaceMetadata;


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
                    .append("runDetails map<text, text>,")
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
                .append("port int,")
                .append("direction text,")
                .append("frameData map<text, text>,")
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
        if(!switchesTableCreated)
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
}
