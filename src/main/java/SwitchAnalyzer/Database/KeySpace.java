package SwitchAnalyzer.Database;

import com.datastax.driver.core.Session;

public class KeySpace {
    private static Session session = DBConnect.getSession();
    private static String replicationStrategy = "NetworkTopologyStrategy";
    private static int numberOfReplicas = 1;
    private static boolean historyKeyspaceCreated = false;

    public void setReplicationStrategy(String replicationStrategy) { this.replicationStrategy = replicationStrategy; }

    public void setNumberOfReplicas(int numberOfReplicas) { this.numberOfReplicas = numberOfReplicas; }
    /**
     * Input: name of keyspace , replication strategy and number of replicas
     * Output: void
     * Description:
     * The function will create the keyspace based on the name , replication strategy
     * and number of replicas passed to the function
     */
    private static void createKeyspace (String keyspaceName , String replicationStrategy , int numberOfReplicas )
    {
        StringBuilder sb = new StringBuilder("CREATE KEYSPACE IF NOT EXISTS ")
                .append(keyspaceName)
                .append(" WITH replication = {")
                .append("'class':'")
                .append(replicationStrategy)
                .append("','replication_factor':")
                .append(numberOfReplicas).append("};");

        final String query = sb.toString();

        session.execute(query);
    }
    /**
     * Input: name of keyspace
     * Output: void
     * Description:
     * The function will check if the keyspace exists or not
     * If the keyspace exists then we will use the keyspace directly
     * If the keyspace does not exist then we will create it first then use it
     * Special case for keyspace history we will check on the boolean historyKeyspaceCreated so if it is already
     * created then we will not have to make a select query to see if it already exists
     */
    public static void useKeyspace_MOM(String keyspaceName){
        if(keyspaceName.toLowerCase() == "history" && historyKeyspaceCreated == false)
        {
                createKeyspace(keyspaceName.toLowerCase() , replicationStrategy , numberOfReplicas);
                historyKeyspaceCreated =true;
        }
        else
        {
            if(!isKeyspaceExists(keyspaceName.toLowerCase())){
                createKeyspace(keyspaceName.toLowerCase() , replicationStrategy , numberOfReplicas);
            }
        }
        useKeyspace_Node(keyspaceName);
    }
    public static void useKeyspace_Node(String keyspaceName ) {
        session.execute("USE " + keyspaceName.toLowerCase());
    }
    /**
     * Input: name of keyspace
     * Output: void
     * Description:
     * The function will delete the keyspace whose name is passed to the function
     * special case for keyspace history if we delete then we will set the boolean false
     */
    public static void deleteKeyspace(String keyspaceName){
        StringBuilder sb = new StringBuilder("DROP KEYSPACE ").append(keyspaceName);
        final String query = sb.toString();
        session.execute(query);
        if(keyspaceName.toLowerCase() == "history")
        {
            historyKeyspaceCreated =false;
        }
    }
    /**
     * Input: name of keyspace
     * Output: boolean indicating whether the keyspace exists
     * Description:
     * The function will check if the keyspace exists or not
     * if the keyspace exists the function will return true
     * if the keyspace does not exist the function will return false
     */
    private static boolean isKeyspaceExists(String keyspaceName)
    {
        // Check if the keyspace exists
        if (DBConnect.getMetadata().getKeyspace(keyspaceName) != null) {
            System.out.println("Keyspace " + keyspaceName + " exists");
            return true;
        } else {
            System.out.println("Keyspace " + keyspaceName + " does not exist");
        }
        return false;
    }

}

