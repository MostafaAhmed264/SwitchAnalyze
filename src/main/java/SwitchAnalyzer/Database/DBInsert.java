package SwitchAnalyzer.Database;

import SwitchAnalyzer.miscellaneous.JSONConverter;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.utils.UUIDs;
import java.util.Map;
import java.util.UUID;

public class DBInsert
{
    public static String keyspaceName;
    public static void setKeyspaceName(String keyspaceName) { DBInsert.keyspaceName = keyspaceName; }
    public static void insertFrame(String frameJson)
    {
        final String query = "INSERT INTO frames_run" + DBConnect.getLastRun() + " JSON '" + frameJson + "';";
        System.out.println(frameJson);
        DBConnect.getSession().execute(query);
    }
    public static void insertRun(Map<String,String> runDetails)
    {
        Run_DB run=new Run_DB();
        run.rundetails=runDetails;
        final String query = "INSERT INTO runs JSON '" + JSONConverter.toJSON(run) + "';";
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
    public static void insertSwitch(Switch_DB dbSwitch)
    {
        if(!isSwitchNameAlreadyExists(dbSwitch.switchName))
        {
            UUID timeUuid = UUIDs.timeBased();
            StringBuilder sb = new StringBuilder("INSERT INTO ").append("switches")
                    .append("(id,switchName,totalNoOfPorts) ").append("VALUES (")
                    .append(timeUuid.toString()).append(", '")
                    .append(dbSwitch.switchName).append("', ")
                    .append(dbSwitch.totalnoofports).append(");");
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
