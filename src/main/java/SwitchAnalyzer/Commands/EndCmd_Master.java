package SwitchAnalyzer.Commands;

import SwitchAnalyzer.Kafka.Topics;
import SwitchAnalyzer.Machines.MachineNode;
import SwitchAnalyzer.MainHandler_MOM;
import SwitchAnalyzer.miscellaneous.GlobalVariable;
import SwitchAnalyzer.miscellaneous.JSONConverter;

import static SwitchAnalyzer.MainHandler_Master.master;

public class EndCmd_Master extends ICommandMaster
{
    public EndCmd_Master(int id) { this.portID = id; }

    public void processCmd()
    {
        GlobalVariable.retrieveDataFromNode = false;
        GlobalVariable.stopRecieving = false;
        GenCmd(0);
    }

    public void GenCmd(int id)
    {
        String json = JSONConverter.toJSON(new EndRunCmd_Node(id));
        json = "6" + json;
        MainHandler_MOM.cmdProducer.produce(json, Topics.cmdFromMOM);
        MainHandler_MOM.cmdProducer.flush();
    }
}
