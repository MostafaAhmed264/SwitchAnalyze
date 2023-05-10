package SwitchAnalyzer.Database;

import SwitchAnalyzer.miscellaneous.JSONConverter;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.utils.UUIDs;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class DBInsert
{
    private static String keyspaceName;
    public static String getKeyspaceName() {
        return keyspaceName;
    }
    public static void setKeyspaceName(String keyspaceName) { DBInsert.keyspaceName = keyspaceName; }
    public static void insertFrame(String frameJson)
    {
        StringBuilder sb = new StringBuilder(
                "INSERT INTO frames_run"+DBConnect.getLastRun()+" JSON '"+frameJson+"';");
        final String query = sb.toString();
        System.out.println(frameJson);
        DBConnect.getSession().execute(query);
    }
    public static void insertRun(Map<String,String> runDetails)
    {
        DBRun run=new DBRun();
        run.rundetails = runDetails;
        run.setRunno_DBInsert();
        StringBuilder sb = new StringBuilder("INSERT INTO runs JSON '"+JSONConverter.toJSON(run)+"';");
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
            StringBuilder sb = new StringBuilder("INSERT INTO ").append("switches")
                    .append("(id,switchName,totalNoOfPorts) ").append("VALUES (")
                    .append(timeUuid.toString()).append(", '")
                    .append(dbSwitch.getSwitchName()).append("', ")
                    .append(String.valueOf(dbSwitch.getTotalNoOfPorts())).append(");");
            final String query = sb.toString();
            System.out.println(query);
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
        StringBuilder sb = new StringBuilder("SELECT * FROM switches WHERE switchName = '")
                .append(switchName).append("'ALLOW FILTERING;");
        final String query = sb.toString();
        ResultSet rs = DBConnect.getSession().execute(query);
        if(rs.isExhausted())
        {
            return false;
        }
        return true;
    }
}
