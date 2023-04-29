package SwitchAnalyzer.Starters;

import SwitchAnalyzer.Network.ExecutorCMD;

public class StarterCRC {

    public static void start()
    {
        ExecutorCMD executor = new ExecutorCMD();
        executor.runCommand("sudo ethtool --offload enp3s0 rx on");
    }
    public static void main(String[] args) {
        start();
    }
}

