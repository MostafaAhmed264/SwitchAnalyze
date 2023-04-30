package SwitchAnalyzer.Collectors;

import SwitchAnalyzer.NamingConventions;

import static SwitchAnalyzer.MainHandler_MOM.masterOfMasters;

public class LatencyCollectorMOM implements Collector
{
    public static long count;

    public String getName() { return NamingConventions.overAllAvgLatency; }
    @Override
    public String collect()
    {
        double avgLatencyHpc = 0;
        double avgLatency = 0;
        double max = Long.MIN_VALUE ;
        double min = Long.MAX_VALUE ;
        String avgLatencyString;

        for (int i = 0; i < masterOfMasters.HPCs.size(); i++)
        {
            avgLatencyString = masterOfMasters.HPCs.get(i).hpcInfo.map.get(NamingConventions.latency);
            avgLatencyHpc += Double.parseDouble(avgLatencyString);
            if (Double.parseDouble(avgLatencyString) > max) { max = Double.parseDouble(avgLatencyString);}
            if (Double.parseDouble(avgLatencyString) < min) { min = Double.parseDouble(avgLatencyString);}
        }
        double currMax = Double.parseDouble(MOMConsumer.results.get(NamingConventions.maxLatency));
        double currMin = Double.parseDouble(MOMConsumer.results.get(NamingConventions.minLatency));
        if (max > currMax) { MOMConsumer.results.put(NamingConventions.maxLatency, String.valueOf(max)); }
        if(min < currMin){ MOMConsumer.results.put(NamingConventions.minLatency ,String.valueOf(min));}
        avgLatencyHpc = avgLatencyHpc / masterOfMasters.HPCs.size();
        avgLatency = Double.parseDouble(MOMConsumer.results.get(NamingConventions.overAllAvgLatency)) + avgLatencyHpc;
        count++;
        return String.valueOf(avgLatency);
    }
}
