package SwitchAnalyzer.Collectors;

import SwitchAnalyzer.Database.DBInsert;
import SwitchAnalyzer.EndCmdUI;
import SwitchAnalyzer.Kafka.GenericConsumer;
import SwitchAnalyzer.Kafka.Topics;
import SwitchAnalyzer.Machines.HPC_INFO;
import SwitchAnalyzer.NamingConventions;
import SwitchAnalyzer.Network.IP;
import SwitchAnalyzer.Network.Ports;
import SwitchAnalyzer.ProduceData_MOM;
import SwitchAnalyzer.Sockets.JettyWebSocketServer;
import SwitchAnalyzer.miscellaneous.GlobalVariable;
import SwitchAnalyzer.miscellaneous.JSONConverter;
import SwitchAnalyzer.miscellaneous.Time;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static SwitchAnalyzer.MainHandler_MOM.masterOfMasters;

/**
 * this class will consume the overall info (for now rates+packet loss) information coming from HPCs through kafka
 * then it will call all the  collectors available in the list to process the data
 * the collectors can be added using the addCollector method
 */

public class MOMConsumer {
    public static ArrayList<Integer> ids = new ArrayList<>();
    static String consumerGroup = GlobalVariable.consumer5;
    static GenericConsumer consumer = new GenericConsumer(IP.ip1 + ":" + Ports.port1, consumerGroup);
    //arraylist of collectors
    public static ArrayList<Collector> collectors = new ArrayList<>();
    /**
     * this map will contain the results of the collectors
     *the key will be the collector name and the value will be the result
     * it is concurrent because it is accessed by multiple threads so it needs to be thread safe
     */
    public static Map<String, String> results = new ConcurrentHashMap<>();

    public static void prepareStats()
    {
        initResultMap();
        while (!GlobalVariable.endRun)
        {
            updateHpcInfo();
            reduce();
            if (GlobalVariable.retrieveDataFromNode)
            {
                ProduceData_MOM.produceData(ids);
            }
        }

        String s = String.valueOf(Double.parseDouble(results.get(NamingConventions.overAllRates))/RatesCollectorMOM.count);
        results.put(NamingConventions.overAllRates, s);
        results.put(NamingConventions.overAllAvgPacketLoss, String.valueOf(Double.parseDouble(results.get(NamingConventions.overAllAvgPacketLoss))/PLossCollectorMOM.count));
        results.put(NamingConventions.overAllAvgLatency, String.valueOf(Double.parseDouble(results.get(NamingConventions.overAllAvgLatency))/LatencyCollectorMOM.count));
        EndCmdUI endcmdUi=new EndCmdUI();
        endcmdUi.mapResultToObj(results);
        DBInsert.insertRun(endcmdUi.objToMap(),endcmdUi.additional);
        String json = JSONConverter.toJSON(endcmdUi);
        System.out.println(json);
        try { JettyWebSocketServer.writeMessage(json); }
        catch (Exception e) { throw new RuntimeException(e); }
        clear();
    }

    public static void clear()
    {
        RatesCollectorMOM.count = 0; PLossCollectorMOM.count = 0; LatencyCollectorMOM.count = 0;
    }

    public static void initResultMap()
    {
        MOMConsumer.results.put(NamingConventions.overAllAvgLatency, "0");
        MOMConsumer.results.put(NamingConventions.maxLatency, "0");
        MOMConsumer.results.put(NamingConventions.minLatency, "0");
        MOMConsumer.results.put(NamingConventions.totalPacketCount, "0");
        MOMConsumer.results.put(NamingConventions.maxRate, "0");
        MOMConsumer.results.put(NamingConventions.minRate, "0");
        MOMConsumer.results.put(NamingConventions.overAllRates, "0");
        MOMConsumer.results.put(NamingConventions.maxPacketLoss, "0");
        MOMConsumer.results.put(NamingConventions.minPacketLoss, "0");
        MOMConsumer.results.put(NamingConventions.overAllAvgPacketLoss, "0");
        MOMConsumer.results.put(NamingConventions.crcError, "0");
    }
    public static void updateHpcInfo()
    {
        consumer.selectTopic(Topics.ratesFromHPCs);
        while (true)
        {
            ConsumerRecords<String, String> records = consumer.consume(Time.waitTime);
            for (ConsumerRecord<String, String> record : records)
            {
                String json = record.value();
                HPC_INFO hpcInfo = JSONConverter.fromJSON(json, HPC_INFO.class);
                masterOfMasters.HPCs.get(hpcInfo.id-1).hpcInfo = hpcInfo;
            }
            if(records.count() > 0)
                break;
        }
    }

    public static void reduce()
    {
        //loop through the arraylist of collectors and create a thread for each one to call the collect method
        List<Thread> threads = new ArrayList<>();
        for (int i = 0; i < collectors.size(); i++)
        {
            final int index = i; // Make a copy of i
            Thread thread = new Thread(new Runnable()
            {
                @Override
                public void run()
                {
                    String res = collectors.get(index).collect();
                    if (!res.equals("-1"))
                        results.put(collectors.get(index).getName(), res);
                }
            });
            threads.add(thread);
            thread.start();
        }
        // Wait for all threads to finish
        for (Thread thread : threads)
        {
            try { thread.join(); }
            catch (InterruptedException e) { System.out.printf("in MasterConsumer: %s%n", e.getMessage()); }
        }
    }
    public static void clearResults () { results.clear(); }
    public static void addCollector(Collector collectorMaster){ collectors.add(collectorMaster); }
    public static void initMOMCollectors()
    {
        collectors.add(new LatencyCollectorMOM());
        collectors.add(new PLossCollectorMOM());
        collectors.add(new RatesCollectorMOM());
        collectors.add(new TotalPacketCollectorMOM());
        collectors.add(new CrcCollector());
        collectors.add(new CountTypeCollectorMOM());
    }
    public static void removeCollector(Collector collectorMaster){ collectors.remove(collectorMaster); }
    public static void clearCollectors(){ collectors.clear(); }
    public static Map<String, String> getResults() { return results; }
}
