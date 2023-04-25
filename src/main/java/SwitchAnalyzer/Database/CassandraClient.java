package SwitchAnalyzer.Database;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CassandraClient {
    private static final Logger LOG = LoggerFactory.getLogger(CassandraClient.class);

    public static void main(String args[]) {
//        DBSwitch dbSwitch =new DBSwitch("switch",10);
//        DBConnect.connect();
//        DBStorage.remainingStorage();
//        System.out.println(DBStorage.getRemainingDiskSpace());
        DBConnect.connect();
        System.out.println(DBSelect.showHistory());
//        String str = DBSelect.selectAllJson_LastRun_SpecificHeaderTest("ARP Header ");
//        String filePath = "output.txt";
//        try {
//            BufferedWriter writer = new BufferedWriter(new FileWriter(filePath));
//            writer.write(str);
//            writer.close();
//            System.out.println("Successfully wrote to the file.");
//        } catch (IOException e) {
//            System.out.println("An error occurred.");
//            e.printStackTrace();
//        }

    }



}

