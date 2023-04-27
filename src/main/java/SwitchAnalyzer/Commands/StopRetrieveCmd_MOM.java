package SwitchAnalyzer.Commands;

import SwitchAnalyzer.Collectors.MOMConsumer;
import SwitchAnalyzer.Kafka.Topics;
import SwitchAnalyzer.MainHandler_MOM;
import SwitchAnalyzer.Network.HardwareObjects.SwitchPort;
import SwitchAnalyzer.miscellaneous.GlobalVariable;
import SwitchAnalyzer.miscellaneous.JSONConverter;

import java.util.ArrayList;

public class StopRetrieveCmd_MOM implements ICommandMOM
{
    ArrayList<Integer> ids;
    public void processCmd()
    {
        GlobalVariable.retrieveDataFromNode = false;
        for (int i : ids)
        {
            GenCmd(new SwitchPort(i));
        }
        clearMomConsumer();
    }

    public void GenCmd(SwitchPort port)
    {
        String json = JSONConverter.toJSON(new StopRetrieveCmd_Master(port.ID));
        json = GlobalVariable.CMD_IDX.STOPRETRIEVE_IDX + json;
        MainHandler_MOM.cmdProducer.produce(json, Topics.cmdFromMOM);
        MainHandler_MOM.cmdProducer.flush();
    }

    public void clearMomConsumer()
    {
        MOMConsumer.clearResults();
        MOMConsumer.clearCollectors();
    }
}
