package SwitchAnalyzer.Collectors;

import SwitchAnalyzer.NamingConventions;

import static SwitchAnalyzer.MainHandler_MOM.masterOfMasters;

public class CrcCollector implements Collector{

    public String collect()
    {
        long totalCount = 0;
        for (int i = 0; i < masterOfMasters.HPCs.size(); i++)
        {
            totalCount += Long.parseLong(masterOfMasters.HPCs.get(i).hpcInfo.map.get(NamingConventions.crcError));
        }
        totalCount += Long.parseLong( MOMConsumer.results.get(NamingConventions.crcError));
        return String.valueOf(totalCount);
    }

    public String getName()
    {
        return NamingConventions.crcError;
    }
}
