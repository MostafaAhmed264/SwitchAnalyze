package SwitchAnalyzer.Commands.MOM;

import SwitchAnalyzer.Commands.ICommandMOM;
import SwitchAnalyzer.Commands.Master.StopRunCmdMaster;
import SwitchAnalyzer.Kafka.Topics;
import SwitchAnalyzer.MainHandler_MOM;
import SwitchAnalyzer.Network.HardwareObjects.SwitchPort;
import SwitchAnalyzer.miscellaneous.JSONConverter;
import SwitchAnalyzer.miscellaneous.SystemMaps;

import java.util.ArrayList;

public class StopRunCmd_MOM implements ICommandMOM
{
    public ArrayList<Integer> ids;


    public void processCmd()
    {
        for (int i : ids) { GenCmd(new SwitchPort(i)); }
    }

    public void GenCmd(SwitchPort port)
    {
        String json = JSONConverter.toJSON(new StopRunCmdMaster(port.ID));
        json = SystemMaps.STOP_RUN_CMD_MASTER_IDX + json;
        MainHandler_MOM.cmdProducer.produce(json, Topics.cmdFromMOM);
        MainHandler_MOM.cmdProducer.flush();
    }
}
