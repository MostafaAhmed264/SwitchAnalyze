package SwitchAnalyzer;

import org.apache.kafka.common.protocol.types.Field;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class EndCmdUI {
    public String CRC_COUNT;
    public String avgOverAllRate;
    public String avgOverAllPacketLoss;
    public String maxPL;
    public String minPL;

    public String maxRate;
    public String minRate;
    public String avgOverAllLatency;
    public String maxLatency;
    public String minLatency;
    public String TotalCount;
    public Map<String, String> additional;
    public static ArrayList<String> keys = new ArrayList<>(Arrays.asList(
            "CRC_COUNT", "avgOverAllRate","avgOverAllPacketLoss","maxPL",
            "minPL","maxRate","minRate","avgOverAllLatency","maxLatency",
            "minLatency","TotalCount"
    ));


    public void mapResultToObj(Map<String ,String> map)
    {
        for(String key : map.keySet())
        {
            switch (key)
            {
                case "crcError": CRC_COUNT = map.get(key);
                map.remove(key);
                break;
                case "avgOverAllRate": avgOverAllRate = map.get(key);
                map.remove(key);
                    break;
                case "avgOverAllPacketLoss": avgOverAllPacketLoss= map.get(key);
                    map.remove(key);
                    break;
                case "maxPL": maxPL= map.get(key);
                    map.remove(key);
                    break;
                case "minPL": minPL= map.get(key);
                    map.remove(key);
                    break;
                case "maxRate": maxRate= map.get(key);
                    map.remove(key);
                    break;
                case "minRate": minRate= map.get(key);
                    map.remove(key);
                    break;

                case "avgOverAllLatency": avgOverAllLatency= map.get(key);
                    map.remove(key);
                    break;
                case "maxLatency": maxLatency= map.get(key);
                    map.remove(key);
                    break;
                case "minLatency": minLatency= map.get(key);
                    map.remove(key);
                    break;
                case "TotalCount": TotalCount= map.get(key);
                    map.remove(key);
                    break;


            }
            additional = map;
        }

    }
    public Map<String, String> objToMap()
    {
        Map<String,String> map = new HashMap<>();
        map.put("CRC_COUNT",CRC_COUNT);
        map.put("avgOverAllRate",avgOverAllRate);
        map.put("avgOverAllPacketLoss",avgOverAllPacketLoss);
        map.put("maxPL",maxPL);
        map.put("minPL",minPL);
        map.put("maxRate",maxRate);
        map.put("minRate",minRate);
        map.put("avgOverAllLatency",avgOverAllLatency);
        map.put("maxLatency",maxLatency);
        map.put("minLatency",minLatency);
        map.put("TotalCount",TotalCount);
        return map;
    }

}
