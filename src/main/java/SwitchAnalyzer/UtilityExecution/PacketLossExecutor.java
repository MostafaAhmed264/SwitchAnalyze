package SwitchAnalyzer.UtilityExecution;

import SwitchAnalyzer.NamingConventions;
import SwitchAnalyzer.Network.PacketLoss.PacketLossCalculate;

public class PacketLossExecutor implements IExecutor
{
    public void execute()
    {
        float pl = PacketLossCalculate.startPacketLossTest();
        UtilityExecutor.result.put(NamingConventions.packetLoss, Float.toString(pl));
        System.out.println("packetloss:"+pl);
    }
}
