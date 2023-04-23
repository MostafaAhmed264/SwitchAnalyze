package SwitchAnalyzer.Database;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Host;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.Token;
import com.datastax.driver.core.utils.UUIDs;
import org.apache.log4j.BasicConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
public class CassandraClient {
    private static final Logger LOG = LoggerFactory.getLogger(CassandraClient.class);

    public static void main(String args[]) {
        DBSwitch dbSwitch =new DBSwitch("switch",10);
//        DBConnect.connectToDB_MOM(dbSwitch);
//       DBConnect.closeConnectionToDB();
       DBConnect.connectToDB_Node("switch");
       DBStorage.remainingStorage();
       System.out.println(DBStorage.getRemainingDiskSpace());
//        DBInsert.preparedStatement(DBConnect.getLastRun());
//        DBInsert.insert(DBConverter.getFrame());
//        DBConnect.closeConnectionToDB();
//         KeySpace.deleteKeyspace("history");
////        KeySpace.deleteKeyspace("abbas");
//      KeySpace.deleteKeyspace("switch");
//        String result_json =DBSelect.selectAllNoConditionsTest();
//        System.out.println(result_json);
        DBConnect.closeConnectionToDB();

        //set replication factor
//        DBConnect.connectToDB_Node("abbas");
//        byte [] b ={1,2,3};
////        Date date = new Date();
////        Timestamp timestamp = new Timestamp(date.getTime());
//        Date time =new Date();
//        DBFrame f =new DBFrame(b,time,1,2,"IPV4","UDP",false,true);
//        DBInsert.insert(f);
//        String result = DBSelect.selectAllNoConditionsTest();
//        System.out.println(result);
        //KeySpace.deleteKeyspace("abbas");
        //KeySpace.deleteKeyspace("history");
        //KeySpace.createKeyspace("Switch","NetworkTopologyStrategy",1);
        //KeySpace.useKeyspace("Switch");
        /*create tables here**/
     /*   byte [] b={1,3,4};
        Frame frame = new Frame(b);
        frame.setRunNo(2);
        DBInsert.beginInsert(frame);
        //System.out.println(FrameRepository.selectAllJSON());*/
        //System.out.println("DB is Connected");
//         byte [] b={1,2,3};
//         Frame frame = new Frame(6,b);
//         DBConnect.insertFrame(frame);
        // DBConnect.selectAllFrames().forEach(o -> LOG.info("ID in frames: "+o.id));
//        for (int i = 0; i < f.size() ; i++) {
//            System.out.println(f.get(i).getID());
//        }
        //DBConnect.createKeyspace("Switch","NetworkTopologyStrategy",2);
        //DBConnect.useKeyspace("Switch");
        //FrameRepository.createFramesTable();
        //DBConnect.useKeyspace("Switch");
       //FrameRepository.deleteTable("Frames");
//        DBConnect.deleteKeyspace("Switch");
//
       //FrameRepository.createFramesTable();
        //FrameRepository.selectAll().forEach(o -> LOG.info("ID in frames: "+o.id));

    }
    public static void start(DBFrame frame)
    {
//        DBSwitch dbSwitch =new DBSwitch("switch",10);
//        DBConnect.connectToDB_MOM(dbSwitch);
//        DBConnect.closeConnectionToDB();

        //DBInsert.preparedStatement(DBConnect.getLastRun());
        DBInsert.insert(frame);
        String result_json =DBSelect.selectAllNoConditionsTest();
        System.out.println(result_json);
    }
}
