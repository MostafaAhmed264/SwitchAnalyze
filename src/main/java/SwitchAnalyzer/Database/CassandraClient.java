package SwitchAnalyzer.Database;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
public class CassandraClient {
    private static final Logger LOG = LoggerFactory.getLogger(CassandraClient.class);

    public static void main(String args[]) {
        DBSwitch dbSwitch =new DBSwitch("switch",10);
        DBConnect.connect();
        DBConnect.startRun(dbSwitch);
       DBConnect.closeConnectionToDB();
       DBConnect.connectToDB_Node("switch");
       DBStorage.remainingStorage();
       System.out.println(DBStorage.getRemainingDiskSpace());
        DBConnect.closeConnectionToDB();

    }
}
