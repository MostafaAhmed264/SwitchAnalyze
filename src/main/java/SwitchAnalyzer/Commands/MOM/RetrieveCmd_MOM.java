package SwitchAnalyzer.Commands.MOM;

import SwitchAnalyzer.Collectors.MOMConsumer;
import SwitchAnalyzer.Commands.ICommandMOM;
import SwitchAnalyzer.Commands.Master.RetrieveCmd_Master;
import SwitchAnalyzer.Kafka.Topics;
import SwitchAnalyzer.Machines.MOM;
import SwitchAnalyzer.MainHandler_MOM;
import SwitchAnalyzer.Network.HardwareObjects.SwitchPort;
import SwitchAnalyzer.ProduceData_MOM;
import SwitchAnalyzer.miscellaneous.GlobalVariable;
import SwitchAnalyzer.miscellaneous.JSONConverter;
import SwitchAnalyzer.miscellaneous.SystemMaps;

import java.util.ArrayList;

public class RetrieveCmd_MOM implements ICommandMOM
{
    public ArrayList<String> retrievals;
    public ArrayList<Integer> ids;

    @Override
    public void processCmd()
    {
        GlobalVariable.retrieveDataFromNode = true;
        MOMConsumer.ids.clear();
        MOMConsumer.ids.addAll(this.ids); 
        for (int i : ids)
        {
            GenCmd(new SwitchPort(i));
        }
    }

    public void GenCmd(SwitchPort port)
    {
        String json = JSONConverter.toJSON(new RetrieveCmd_Master(port.ID, this.retrievals));
        json = SystemMaps.RETRIEVE_CMD_MASTER_IDX + json;
        MainHandler_MOM.cmdProducer.produce(json, Topics.cmdFromMOM);
        MainHandler_MOM.cmdProducer.flush();
    }
}
