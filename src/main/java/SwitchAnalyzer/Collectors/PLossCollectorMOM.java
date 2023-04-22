package SwitchAnalyzer.Collectors;

import SwitchAnalyzer.NamingConventions;

import static SwitchAnalyzer.MainHandler_MOM.masterOfMasters;

/**
 * this class is responsible for collecting the rates from the HPCs
 * do some processing and then send return the overall rates so that
 * the collector must have a name so that the master can identify it
 */
public class PLossCollectorMOM implements Collector {
    private String name = "PacketLoss";
    public String getName() {
        return name;
    }
    @Override
    public String collect()
    {

        float packetLoss = 0;
        String packetLossString;

        for (int i = 0; i < masterOfMasters.HPCs.size(); i++) {
            //convert the string to a float
            packetLossString = masterOfMasters.HPCs.get(i).hpcInfo.map.get(NamingConventions.packetLoss);
            packetLoss += Float.parseFloat(packetLossString);
        }
        return String.valueOf(packetLoss);

    }
}
