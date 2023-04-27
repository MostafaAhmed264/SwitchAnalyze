package SwitchAnalyzer.Collectors;

import SwitchAnalyzer.NamingConventions;

import static SwitchAnalyzer.MainHandler_Master.master;

/**
 * this class is responsible for collecting the rates from the machines
 * do some processing and then send return the overall rates so that
 * the master can send it to the MOM
 * the collector must have a name so that the master can identify it
 */
public class PLossCollectorMaster implements Collector {
    private String name = "PacketLoss";
    public String getName() {
        return name;
    }
    @Override
    public String collect()
    {
        float packetLoss = 0;
        String packetLossString;

        for (int i = 0; i < master.childNodes.size(); i++) {
            //convert the string to a float
            packetLossString = master.childNodes.get(i).machineInfo.map.get(NamingConventions.packetLoss);
            packetLoss += Float.parseFloat(packetLossString);
        }

        return String.valueOf(packetLoss/master.childNodes.size());

    }

}
