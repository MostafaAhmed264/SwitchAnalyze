package SwitchAnalyzer.Collectors;

import SwitchAnalyzer.NamingConventions;

import static SwitchAnalyzer.MainHandler_Master.master;

public class LatencyCollectorMaster implements Collector
{
    private final String name = NamingConventions.latency;

    public String collect() {
        long latency = 0;
        String latencyString;

        for (int i = 0; i < master.childNodes.size(); i++) {
            //convert the string to a float
            latencyString = master.childNodes.get(i).machineInfo.map.get(NamingConventions.latency);
            latency += Float.parseFloat(latencyString);
        }

        return String.valueOf(latency/master.childNodes.size());
    }

    public String getName() {
        return name;
    }
}
