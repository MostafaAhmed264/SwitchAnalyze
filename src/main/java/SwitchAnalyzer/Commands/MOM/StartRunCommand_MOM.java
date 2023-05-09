package SwitchAnalyzer.Commands.MOM;

import SwitchAnalyzer.Collectors.MOMConsumer;
import SwitchAnalyzer.Commands.ICommandMOM;
import SwitchAnalyzer.Commands.Master.StartRunALL;
import SwitchAnalyzer.Commands.Master.StartRunCommand_Master;
import SwitchAnalyzer.Kafka.Topics;
import SwitchAnalyzer.MainHandler_MOM;
import SwitchAnalyzer.Network.HardwareObjects.SwitchPortPair;
import SwitchAnalyzer.miscellaneous.GlobalVariable;
import SwitchAnalyzer.miscellaneous.JSONConverter;
import SwitchAnalyzer.miscellaneous.SystemMaps;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class StartRunCommand_MOM implements ICommandMOM
{
    int saveOption;
    boolean DefRun;
    ArrayList<SwitchPortPair> pairs = new ArrayList<>();
    @Override
    /*
        Propagate The command AND Broadcast
        startTracking the results and updating the results map
     */
    public void processCmd()
    {
        for (int i = 0; i < pairs.size(); ++i)
        {
            if (pairs.get(i).fromPort.portConfig.packetInfos.size() == 0)
            {
                pairs.remove(i);
                i--;
            }
        }
        System.out.println(JSONConverter.toJSON(this));
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Calendar cal = Calendar.getInstance();
        MOMConsumer.results.put("StartTime", dateFormat.format(cal.getTime()));
        genStartRunALL();
        if (DefRun) { for (SwitchPortPair switchPortPair : GlobalVariable.defPairs) { GenCmd(switchPortPair); } }
        else { for (SwitchPortPair switchPort : pairs) { GenCmd(switchPort); } }
        startTrackingModuleThread();
    }

    public static void startTrackingModuleThread()
    {
        MOMConsumer.initMOMCollectors();
        Thread t = new Thread(MOMConsumer::prepareStats);
        t.start();
    }
    public void GenCmd(SwitchPortPair portPair)
    {
        String startRunJSON = JSONConverter.toJSON(new StartRunCommand_Master(portPair));
        startRunJSON = SystemMaps.START_RUN_CMD_MASTER_IDX + startRunJSON;
        System.out.println(startRunJSON);
        MainHandler_MOM.cmdProducer.produce(startRunJSON,Topics.cmdFromMOM);
        MainHandler_MOM.cmdProducer.flush();
    }

    public void genStartRunALL()
    {
        String startRunAllJSON = JSONConverter.toJSON(new StartRunALL(saveOption, GlobalVariable.switchName));
        startRunAllJSON = SystemMaps.START_RUN_ALL_CMD_MASTER_IDX + startRunAllJSON;
        MainHandler_MOM.cmdProducer.produce(startRunAllJSON,Topics.cmdFromMOM);
    }

}
