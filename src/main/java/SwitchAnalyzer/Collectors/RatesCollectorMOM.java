package SwitchAnalyzer.Collectors;

import SwitchAnalyzer.Machines.MasterOfHPC;
import SwitchAnalyzer.NamingConventions;
import SwitchAnalyzer.miscellaneous.GlobalVariable;

import static SwitchAnalyzer.MainHandler_MOM.masterOfMasters;

/**
 * this class is responsible for collecting the packetLoss from the machines
 * do some processing and then send return the overall packetLoss so that
 * the master can send it to the MOM
 * the collector must have a name so that the master can identify it
 * note I assumed that the the MasterOfHPC already have the machines in its list
 */
public class RatesCollectorMOM implements Collector
{
    public static long count = 0;
    public static MasterOfHPC myHPC ;
    //the name of the collector is used to identify the collector in the results map
    private String name = "Rates";
    public String getName() { return name; }
    @Override
    public String collect()
    {
        double OverallRate = 0;
        double avgHpcRate = 0;
        double max = Long.MIN_VALUE ;
        double min = Long.MAX_VALUE ;
        String avgHpcRateString;

        for (int i = 0; i < masterOfMasters.HPCs.size(); i++)
        {
            avgHpcRateString = masterOfMasters.HPCs.get(i).hpcInfo.map.get(NamingConventions.rates);
            avgHpcRate += Double.parseDouble(avgHpcRateString);
            if (Double.parseDouble(avgHpcRateString) > max) { max = Double.parseDouble(avgHpcRateString);}
            if (Double.parseDouble(avgHpcRateString) < min) { min = Double.parseDouble(avgHpcRateString);}
        }
        double currMax = Double.parseDouble(MOMConsumer.results.get(NamingConventions.maxRate));
        double currMin = Double.parseDouble(MOMConsumer.results.get(NamingConventions.minRate));
        if (max > currMax) { MOMConsumer.results.put(NamingConventions.maxRate, String.valueOf(max)); }
        if(min < currMin){ MOMConsumer.results.put(NamingConventions.minRate ,String.valueOf(min));}
        avgHpcRate = avgHpcRate/masterOfMasters.HPCs.size();
        OverallRate = Double.parseDouble(MOMConsumer.results.get(NamingConventions.overAllRates)) + avgHpcRate;
        count++;
        return String.valueOf(OverallRate);
    }

}
