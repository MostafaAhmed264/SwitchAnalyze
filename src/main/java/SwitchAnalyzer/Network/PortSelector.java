package SwitchAnalyzer.Network;

import SwitchAnalyzer.Machines.MasterOfHPC;
import SwitchAnalyzer.MainHandler_Master;
import SwitchAnalyzer.UtilityExecution.RateExecutor;
import SwitchAnalyzer.miscellaneous.GlobalVariable;

import java.util.Random;

public class PortSelector
{
    //selects a random hpc to access it for sending to it
    public static MasterOfHPC selectRandomly()
    {
        Random rand = new Random();
        MasterOfHPC hpc = GlobalVariable.portHpcMap.get(rand.nextInt(GlobalVariable.portHpcMap.size()));
        if (hpc.getHPCID() == MainHandler_Master.master.getHPCID())
        {
            return GlobalVariable.portHpcMap.get((hpc.getHPCID() + 1) % GlobalVariable.portHpcMap.size());
        }
        return hpc;
    }
    //select HPC of specific port
    public static MasterOfHPC selectForPort(int portID)
    {
        if (portID == -1) return selectRandomly();
        return GlobalVariable.portHpcMap.get(portID);
    }

    public static int selectRandomPort()
    {
        Random rand = new Random();
        int number = rand.nextInt(GlobalVariable.portHpcMap.size());
        if (number == MainHandler_Master.master.getHPCID())
        {
            return (number+ 1) % GlobalVariable.portHpcMap.size();
        }
        return number;
    }

}
