package SwitchAnalyzer.Commands;

import SwitchAnalyzer.Kafka.Topics;
import SwitchAnalyzer.Machines.MachineNode;
import SwitchAnalyzer.MainHandler_Master;
import SwitchAnalyzer.miscellaneous.JSONConverter;

import static SwitchAnalyzer.MainHandler_Master.master;

public class ResumeRunCmd_Master extends ICommandMaster
{
    public ResumeRunCmd_Master(int portID) { this.portID = portID; }

    @Override
    public void processCmd()
    {
        for (MachineNode node : master.childNodes) { GenCmd(node.getMachineID()); }
    }

    @Override
    public void GenCmd(int id)
    {
        String json = JSONConverter.toJSON(new StopRunCmd_Node(id));
        json = "4"+json;
        MainHandler_Master.cmdProducer.produce(json, Topics.cmdFromHpcMaster);
        MainHandler_Master.cmdProducer.flush();
    }
}
