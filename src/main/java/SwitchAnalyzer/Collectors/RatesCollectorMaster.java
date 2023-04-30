package SwitchAnalyzer.Collectors;

import SwitchAnalyzer.Machines.MasterOfHPC;
import SwitchAnalyzer.NamingConventions;

import static SwitchAnalyzer.MainHandler_Master.master;

/**
 * this class is responsible for collecting the packetLoss from the machines
 * do some processing and then send return the overall packetLoss so that
 * the master can send it to the MOM
 * the collector must have a name so that the master can identify it
 * note I assumed that the the MasterOfHPC already have the machines in its list
 */
public class RatesCollectorMaster implements Collector {
    public static MasterOfHPC myHPC ;
    //the name of the collector is used to identify the collector in the results map
    private String name = "Rates";
    public String getName() {
        return name;
    }
    @Override
    public String collect()
    {
            float OverallRate;
            // this variable is made because the result from is the map is a string
            String overAllRateString;
            OverallRate= 0;
            for (int i = 0; i < master.childNodes.size(); i++)
            {
                //convert the string to a float
                System.out.println(master.childNodes.get(i).machineInfo.map);
                overAllRateString = master.childNodes.get(i).machineInfo.map.get(NamingConventions.rates);
                OverallRate += Float.parseFloat(overAllRateString);
            }
            return String.valueOf(OverallRate);
    }

}
