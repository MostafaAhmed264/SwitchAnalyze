package SwitchAnalyzer.Database;

import SwitchAnalyzer.miscellaneous.JSONConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class CassandraClient {
    private static final Logger LOG = LoggerFactory.getLogger(CassandraClient.class);

    public static void main(String args[]) {
//        DBSwitch dbSwitch =new DBSwitch("switch",10);
//        DBConnect.connect();
//        DBStorage.remainingStorage();
//        System.out.println(DBStorage.getRemainingDiskSpace());
        ArrayList<String> s = new ArrayList<>();
        s.add("asdads");
        s.add("SDadskja");
        s.add("ASDADS") ;
        System.out.println(JSONConverter.toJSON(s));

//        ArrayList<String> switchNames = new ArrayList<>();
//        switchNames.add("wafy");
//        switchNames.add("wafy");
//        ArrayList<Long> runNos = new ArrayList<>();
//        runNos.add(1L);
//        runNos.add(1L);
//        //ArrayList<String> str = DBSelect.compareRuns(switchNames,runNos);
////        String str = DBSelect.selectAllJson_LastRun_SpecificHeaderTest("ARP Header ");
//        String filePath = "output.txt";
//        try {
//            BufferedWriter writer = new BufferedWriter(new FileWriter(filePath));
//            writer.write(str.get(0));
//            writer.write(str.get(1));
//            writer.close();
//            System.out.println("Successfully wrote to the file.");
//        } catch (IOException e) {
//            System.out.println("An error occurred.");
//            e.printStackTrace();
//        }

    }



}

