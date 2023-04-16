package SwitchAnalyzer.Commands;

import SwitchAnalyzer.Kafka.Topics;
import SwitchAnalyzer.MainHandler_MOM;
import SwitchAnalyzer.Network.HardwareObjects.SwitchPortPair;
import SwitchAnalyzer.miscellaneous.JSONConverter;

import java.util.ArrayList;

public class StartRunCommand_MOM implements ICommandMOM
{
    ArrayList<SwitchPortPair> pairs= new ArrayList<>();
    @Override
    public void processCmd()
    {
        for (SwitchPortPair switchPort : pairs)
        {
           GenCmd(switchPort);
        }
    }

    public void GenCmd(SwitchPortPair portPair)
    {
        String json = JSONConverter.toJSON(new StartRunCommand_Master(portPair));
        System.out.println("the command is : " + json);
        json = "0"+json;
        MainHandler_MOM.cmdProducer.produce(json,Topics.cmdFromMOM);
        MainHandler_MOM.cmdProducer.flush();
    }
}
