package SwitchAnalyzer.Commands;

import SwitchAnalyzer.Collectors.MOMConsumer;
import SwitchAnalyzer.Kafka.Topics;
import SwitchAnalyzer.MainHandler_MOM;
import SwitchAnalyzer.MapPacketInfo;
import SwitchAnalyzer.Network.ErrorDetection.CRC;
import SwitchAnalyzer.Network.ErrorDetection.None;
import SwitchAnalyzer.Network.HardwareObjects.SwitchPortPair;
import SwitchAnalyzer.Network.PacketInfo;
import SwitchAnalyzer.Sockets.PacketInfoGui;
import SwitchAnalyzer.miscellaneous.GlobalVariable;
import SwitchAnalyzer.miscellaneous.JSONConverter;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class StartRunCommand_MOM implements ICommandMOM
{
    int saveOption;
    boolean defRun;
    ArrayList<SwitchPortPair> pairs = new ArrayList<>();
    @Override
    public void processCmd()
    {
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Calendar cal = Calendar.getInstance();
        MOMConsumer.results.put("StartTime", dateFormat.format(cal.getTime()));

        if (defRun)
        {
            for (SwitchPortPair switchPortPair : GlobalVariable.defPairs) { GenCmd(switchPortPair); }
        }
        else { for (SwitchPortPair switchPort : pairs) { GenCmd(switchPort); } }
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
        String startRunJSON = JSONConverter.toJSON(new StartRunCommand_Master(portPair, saveOption, GlobalVariable.switchName));
        String startRecJson = JSONConverter.toJSON(buildStartRecieveMaster(portPair));
        startRunJSON = GlobalVariable.CMD_IDX.STARTRUN_IDX + startRunJSON;
        startRecJson = GlobalVariable.CMD_IDX.STARTRECIEVE_IDX + startRecJson;
        MainHandler_MOM.cmdProducer.produce(startRunJSON,Topics.cmdFromMOM);
        MainHandler_MOM.cmdProducer.produce(startRecJson,Topics.cmdFromMOM);
        MainHandler_MOM.cmdProducer.flush();
    }

    private StartRecieve_Master buildStartRecieveMaster(SwitchPortPair portPair)
    {
        StartRecieve_Master startRecieve_master = new StartRecieve_Master();
        startRecieve_master.errorDetectingAlgorithm = new None();
        if (isCrc(portPair.fromPort.portConfig.packetInfos)) { startRecieve_master.errorDetectingAlgorithm = new CRC(); }
        startRecieve_master.portID = 0;
        return startRecieve_master;
    }
}
