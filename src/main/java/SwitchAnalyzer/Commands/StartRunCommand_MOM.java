package SwitchAnalyzer.Commands;

import SwitchAnalyzer.Database.DBConnect;
import SwitchAnalyzer.Database.DBSwitch;
import SwitchAnalyzer.Kafka.Topics;
import SwitchAnalyzer.MainHandler_MOM;
import SwitchAnalyzer.MapPacketInfo;
import SwitchAnalyzer.Network.ErrorDetection.CRC;
import SwitchAnalyzer.Network.ErrorDetection.ErrorDetectingAlgorithms;
import SwitchAnalyzer.Network.ErrorDetection.None;
import SwitchAnalyzer.Network.HardwareObjects.SwitchPortPair;
import SwitchAnalyzer.Network.PacketInfo;
import SwitchAnalyzer.Sockets.PacketInfoGui;
import SwitchAnalyzer.miscellaneous.JSONConverter;

import java.util.ArrayList;

public class StartRunCommand_MOM implements ICommandMOM
{
    ArrayList<SwitchPortPair> pairs= new ArrayList<>();
    @Override
    public void processCmd()
    {
        //DBConnect.startRun(new DBSwitch("wafy", 10));
        for (SwitchPortPair switchPort : pairs)
        {
           GenCmd(switchPort);
        }
    }

    public static boolean isCrc(ArrayList<PacketInfoGui> packetInfoGuis)
    {
        for (PacketInfoGui packetInfoGui : packetInfoGuis)
        {
            if (((PacketInfo) new MapPacketInfo().map(packetInfoGui)).errorDetectingAlgorithm instanceof CRC)
            {
                return true;
            }
        }
        return false;
    }

    public void GenCmd(SwitchPortPair portPair)
    {
        String json = JSONConverter.toJSON(new StartRunCommand_Master(portPair));
        StartRecieve_Master startRecieve_master = new StartRecieve_Master();
        startRecieve_master.errorDetectingAlgorithm = new None();
        if (isCrc(portPair.fromPort.portConfig.packetInfos)) {startRecieve_master.errorDetectingAlgorithm = new CRC(); }
        startRecieve_master.portID = 0;
        String json2 = JSONConverter.toJSON(startRecieve_master);
        System.out.println("the command is : " + json);
        System.out.println("Receive cmd is" + json2);
        json = "0"+json;
        json2 = "5" + json2;
        MainHandler_MOM.cmdProducer.produce(json,Topics.cmdFromMOM);
        MainHandler_MOM.cmdProducer.produce(json2,Topics.cmdFromMOM);
        MainHandler_MOM.cmdProducer.flush();
    }
}
