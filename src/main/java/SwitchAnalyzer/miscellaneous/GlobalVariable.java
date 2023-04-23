package SwitchAnalyzer.miscellaneous;

import SwitchAnalyzer.Kafka.GenericProducer;
import SwitchAnalyzer.Machines.MasterOfHPC;
import SwitchAnalyzer.Network.Header;

import java.util.HashMap;
import java.util.Map;

public class GlobalVariable
{
    public static int storageClass = 0;
    public static volatile boolean retrieveDataFromNode;
    public static Map<Integer, MasterOfHPC> portHpcMap = new HashMap<Integer,MasterOfHPC>();
    public static String interfaceName = "enp2s0";
    public static int webSocketPort = 9099;
    public static int webSocketMaxMessages = 100000;
    public static boolean stopRunSignal = false;
    public static boolean stopRecieving = false;
}
