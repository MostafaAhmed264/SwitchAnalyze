package SwitchAnalyzer.miscellaneous;

import SwitchAnalyzer.Machines.MasterOfHPC;
import SwitchAnalyzer.Network.HardwareObjects.SwitchPortPair;
import SwitchAnalyzer.Sockets.PacketInfoGui;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class GlobalVariable
{
    public enum CMD_IDX
    {
        STARTRUN_IDX,  RETRIEVE_CMD_IDX, STOPRETRIEVE_IDX, PAUSESEND_IDX, RESUMESEND_IDX, STARTRECIEVE_IDX, ENDRUN_IDX, SHOWHISTORY_IDX, SAVESWITCH_IDX, GETFRAMES_IDX
    }
    public static int storageClass = 0;
    public static volatile boolean retrieveDataFromNode;
    public static Map<Integer, MasterOfHPC> portHpcMap = new HashMap<Integer,MasterOfHPC>();
    public static String interfaceName = "enp2s0";
    public static int webSocketPort = 9099;
    public static int webSocketMaxMessages = 100000;
    public static boolean stopRunSignal = false;
    public static boolean stopRecieving = false;
    public static boolean stopSending = false;
    public static String switchName = null;
    public static ArrayList<PacketInfoGui> defaultPacketInfos = new ArrayList<>(
            Arrays.asList
            (
                new PacketInfoGui
                (
                        "Ethernet",
                        "ipv4",
                        "udp",
                        "Taftaf is very very not bad",
                        "CRC",
                        1000,
                        50000,
                        true
                ),
                new PacketInfoGui
                (
                       "Ethernet",
                       "ipv4",
                       "tcp",
                       "Tawfik is very good",
                       "None",
                       1000,
                       50000,
                       false
                )
            )
    );
    public static ArrayList<String> defUtilities = new ArrayList<>(
            Arrays.asList(
                    "Rates","PacketLoss"
                    )
    );
   public static ArrayList<SwitchPortPair> defPairs = new ArrayList<>();
   public static boolean endRun = false;
}
