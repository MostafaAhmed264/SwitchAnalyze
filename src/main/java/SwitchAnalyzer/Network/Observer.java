package SwitchAnalyzer.Network;


import SwitchAnalyzer.miscellaneous.GlobalVariable;

public class Observer
{
    // Returns the send and receive rates in kilobits per second
    public static float getRate()
    {
        float sendRate;
        String output;
        String buffSend = "";
        String buffRec = "";
        int outputIndx = 3;
        ExecutorCMD system = new ExecutorCMD();
        system.runCommand("sudo ./netscript.sh "+ GlobalVariable.interfaceName); // Run script in shell
        output = system.outputStore; // Output from terminal (command)
        // Get send rate
        while (output.charAt(outputIndx) != ':')
        {
            outputIndx++;
        }
        outputIndx += 2; // Was at ':' now first digit from rate
        while (output.charAt(outputIndx) != ' ')
        {
            buffSend += output.charAt(outputIndx);
            outputIndx++;
        }
        return Float.parseFloat(buffSend);
    }
}
