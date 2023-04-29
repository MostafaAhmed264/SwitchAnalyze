package SwitchAnalyzer.Collectors;

import SwitchAnalyzer.NamingConventions;

import static SwitchAnalyzer.MainHandler_MOM.masterOfMasters;

/**
 * this class is responsible for collecting the rates from the HPCs
 * do some processing and then send return the overall rates so that
 * the collector must have a name so that the master can identify it
 */
public class PLossCollectorMOM implements Collector {
    public static long count;

    public String getName() { return "PacketLoss"; }
    @Override
    public String collect()
    {
        double avgPlHpc = 0;
        double avgPl;
        double max = Long.MIN_VALUE ;
        double min = Long.MAX_VALUE ;
        String avgPlString;

        for (int i = 0; i < masterOfMasters.HPCs.size(); i++)
        {
            System.out.println(masterOfMasters.HPCs.get(0).hpcInfo.map);
            avgPlString = masterOfMasters.HPCs.get(i).hpcInfo.map.get(NamingConventions.packetLoss);
            avgPlHpc += Double.parseDouble(avgPlString);
            if (Double.parseDouble(avgPlString) > max) { max = Double.parseDouble(avgPlString);}
            if (Double.parseDouble(avgPlString) < min) { min = Double.parseDouble(avgPlString);}
        }
        double currMax = Double.parseDouble(MOMConsumer.results.get(NamingConventions.maxPacketLoss));
        double currMin = Double.parseDouble(MOMConsumer.results.get(NamingConventions.minPacketLoss));
        if (max > currMax) { MOMConsumer.results.put(NamingConventions.maxPacketLoss, String.valueOf(max)); }
        if(min < currMin){ MOMConsumer.results.put(NamingConventions.minPacketLoss ,String.valueOf(min));}
        avgPlHpc = avgPlHpc / masterOfMasters.HPCs.size();
        avgPl = Double.parseDouble(MOMConsumer.results.get(NamingConventions.overAllAvgPacketLoss)) + avgPlHpc;
        count++;
        return String.valueOf(avgPl);
    }
}
