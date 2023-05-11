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
    public static Map<Integer, MasterOfHPC> portHpcMap = new HashMap<Integer,MasterOfHPC>();
    public static String interfaceName;
    public static int webSocketPort = 9099;
    public static int webSocketMaxMessages = 100000;
    public static boolean stopRunSignal = false; //FOR NODE ONLY PAUSES THE RUN USING WAIT AND NOTIFY
    public static boolean stopRecieving = false; //FOR MASTER ONLY AND STOPS THE CONSUMING FROM KAFA BYTES TOPIC
    public static boolean stopSending = false; //FOR NODE ONLY USED TO END THE SENDING BY BREAKING FROM THE SENDING LOOP
    public static boolean endRun = false; //USED FOR CONTROLLING THE TRACKING MODULE IN MOM SO THAT IT WOULD START TRACKING AT THE BEGINNING OF EACH RUN AND CLOSES BY ENDRUN
    /*
        USED FOR:
        1 - STOPRETRIEVE CMD MOM (STOPS SENDING IN WEB SOCKET FOR INFO NOT PACKETS)
        2 - RETRIEVE CMD MOM (ENABLES SENDING IN WEB SOCKET FOR INFO NOT PACKETS)
        3 - STARTRUNCOMMAND_MASTER (STARTS PRODUCING IN KAFKA FOR MASTER)
        4 - STARTRUNCOMMAND_NODE (STARTS PRODUCING IN KAFKA FOR NODE)
        5 - USED  in ENDRUN MASTER & MOM TO STOP PRODUCING IN KAFKA
     */
    public static volatile boolean retrieveDataFromNode;

    public static String consumer1 = "cons1@#11111ADSAD";
    public static String consumer2 = "cons2@#22222ASD";
    public static String consumer3 = "cons3@#33333ASD";
    public static String consumer4 = "cons4@#44444ASD";
    public static String consumer5 = "cons5@#55555ASD";
    public static String consumer6 = "cons6@#66666ASD";

    public static boolean retreiveProcessedFramesFromHPC;

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
}
