package SwitchAnalyzer.Commands.MOM;

import SwitchAnalyzer.Commands.ICommandMOM;
import SwitchAnalyzer.Commands.Master.StopRetrieveCmd_Master;
import SwitchAnalyzer.Kafka.Topics;
import SwitchAnalyzer.MainHandler_MOM;
import SwitchAnalyzer.Network.HardwareObjects.SwitchPort;
import SwitchAnalyzer.miscellaneous.GlobalVariable;
import SwitchAnalyzer.miscellaneous.JSONConverter;
import SwitchAnalyzer.miscellaneous.SystemMaps;

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
    }

    public void GenCmd(SwitchPort port)
    {
        String json = JSONConverter.toJSON(new StopRetrieveCmd_Master(port.ID));
        json = SystemMaps.STOP_RETRIEVE_CMD_MASTER_IDX + json;
        MainHandler_MOM.cmdProducer.produce(json, Topics.cmdFromMOM);
        MainHandler_MOM.cmdProducer.flush();
    }

}
