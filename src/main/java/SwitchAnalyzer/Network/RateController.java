package SwitchAnalyzer.Network;

import SwitchAnalyzer.miscellaneous.GlobalVariable;

public class RateController
{
    public static void setRate(String interfaceName, String sendRate, String receiveRate) throws Exception {
        String command = "sudo wondershaper " + GlobalVariable.interfaceName + " " + receiveRate + " " + sendRate;
        System.out.println(command);
        ExecutorCMD system = new ExecutorCMD();
        system.runCommand(command); // Run script in shell
    }

    // return interface to default state (Max rate)
    public static void setDefault(String interfaceName) throws Exception {
        String command = "sudo wondershaper clear " + GlobalVariable.interfaceName;
        ExecutorCMD system = new ExecutorCMD();
        system.runCommand(command); // Run script in shell
        //String output = system.outputStore; // Output from terminal (command)
    }

    public static void test() throws Exception {
        setRate(GlobalVariable.interfaceName, "10000", "10");
    }
}