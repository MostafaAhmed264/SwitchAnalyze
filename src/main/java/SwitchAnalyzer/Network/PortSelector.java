package SwitchAnalyzer.Network;

import SwitchAnalyzer.Machines.MasterOfHPC;
import SwitchAnalyzer.MainHandler_Master;
import SwitchAnalyzer.UtilityExecution.RateExecutor;
import SwitchAnalyzer.miscellaneous.GlobalVariable;

import java.sql.SQLOutput;
import java.util.Random;

public class PortSelector
{
    //selects a random hpc to access it for sending to it
    public static MasterOfHPC selectRandomly()
    {
        Random rand = new Random();
        MasterOfHPC hpc = GlobalVariable.portHpcMap.get(rand.nextInt(GlobalVariable.portHpcMap.size()) + 1);
        if (hpc.getHPCID() == MainHandler_Master.master.getHPCID())
        {
            return GlobalVariable.portHpcMap.get(((hpc.getHPCID() + 1) % GlobalVariable.portHpcMap.size()) + 1);
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
        System.out.println("line 35 " +rand.nextInt(GlobalVariable.portHpcMap.size()) + 1);
        int number = rand.nextInt(GlobalVariable.portHpcMap.size()) + 1;
        System.out.println("porthpcmap size"+GlobalVariable.portHpcMap.size());
        System.out.println("line 38 " + MainHandler_Master.master);
        if (number == MainHandler_Master.master.getHPCID())
        {
            number=((number+ 1) % (GlobalVariable.portHpcMap.size() + 1));
            if(number==0){
                number=1;
            }
        }
        return number;
    }
    public static int selectRandomPort2()
    {
        Random rand = new Random();
        System.out.println("line 35 " +rand.nextInt(GlobalVariable.portHpcMap.size()) + 1);
        int number = rand.nextInt(GlobalVariable.portHpcMap.size()) + 1;
        System.out.println("porthpcmap size"+GlobalVariable.portHpcMap.size());


        return number;
    }

}
