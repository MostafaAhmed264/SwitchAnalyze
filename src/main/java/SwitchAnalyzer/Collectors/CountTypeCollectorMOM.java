package SwitchAnalyzer.Collectors;

import SwitchAnalyzer.Machines.MOM;
import SwitchAnalyzer.NamingConventions;

import java.util.ArrayList;
import java.util.Set;

import static SwitchAnalyzer.MainHandler_MOM.masterOfMasters;

public class CountTypeCollectorMOM implements Collector
{
    public String collect()
    {
        ArrayList<String> subsetKeys = new ArrayList<>();
        long totalCount = 0;
        for (int i = 0; i < masterOfMasters.HPCs.size(); i++)
        {
            subsetKeys = getKeys(masterOfMasters.HPCs.get(i).hpcInfo.map.keySet());
            for (String key: subsetKeys)
            {
                long currTypeNoPacketsFromSingleHPC = Long.parseLong(masterOfMasters.HPCs.get(i).hpcInfo.map.get(key));
                MOMConsumer.results.putIfAbsent(key.substring(3), "0");
                long prev = Long.parseLong(MOMConsumer.results.get(key.substring(3)));
                MOMConsumer.results.put(key.substring(3),String.valueOf(currTypeNoPacketsFromSingleHPC + prev));
            }
        }
        return "-1";
    }
    public String getName()
    {
        return NamingConventions.countTypeAmount;
    }

    public ArrayList<String> getKeys (Set<String> keys)
    {
        ArrayList<String> result = new ArrayList<>();
        for (String key : keys)
        {
            if(key.startsWith("XXX"))
            {
               result.add(key);
            }
        }
        return result;
    }


}
