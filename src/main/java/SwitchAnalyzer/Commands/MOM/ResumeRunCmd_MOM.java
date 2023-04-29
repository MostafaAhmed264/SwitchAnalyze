package SwitchAnalyzer.Commands.MOM;

import SwitchAnalyzer.Commands.ICommandMOM;
import SwitchAnalyzer.Commands.Master.ResumeRunCmd_Master;
import SwitchAnalyzer.Kafka.Topics;
import SwitchAnalyzer.MainHandler_MOM;
import SwitchAnalyzer.Network.HardwareObjects.SwitchPort;
import SwitchAnalyzer.miscellaneous.JSONConverter;

import java.util.ArrayList;

public class ResumeRunCmd_MOM implements ICommandMOM
{
    public ArrayList<Integer> ids;

    public void processCmd()
    {
        for (int i : ids) { GenCmd(new SwitchPort(i)); }
    }

    public void GenCmd(SwitchPort port)
    {
        String json = JSONConverter.toJSON(new ResumeRunCmd_Master(port.ID));
        json = "4" + json;
        MainHandler_MOM.cmdProducer.produce(json, Topics.cmdFromMOM);
        MainHandler_MOM.cmdProducer.flush();
    }
}
