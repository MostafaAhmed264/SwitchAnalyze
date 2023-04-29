package SwitchAnalyzer.Commands.Master;

import SwitchAnalyzer.Commands.ICommandMaster;
import SwitchAnalyzer.Commands.Node.ResumeRunCmd_Node;
import SwitchAnalyzer.Kafka.Topics;
import SwitchAnalyzer.Machines.MachineNode;
import SwitchAnalyzer.MainHandler_Master;
import SwitchAnalyzer.miscellaneous.JSONConverter;
import SwitchAnalyzer.miscellaneous.SystemMaps;

import static SwitchAnalyzer.MainHandler_Master.master;

public class ResumeRunCmd_Master extends ICommandMaster
{
    public ResumeRunCmd_Master(int portID) { this.portID = portID; }

    /*
        JUST PROPAGATES THE CMD DOWN
     */
    @Override
    public void processCmd()
    {
        for (MachineNode node : master.childNodes) { GenCmd(node.getMachineID()); }
    }

    @Override
    public void GenCmd(int id)
    {
        String json = JSONConverter.toJSON(new ResumeRunCmd_Node(id));
        json = SystemMaps.RESUME_RUN_CMD_NODE_IDX + json;
        MainHandler_Master.cmdProducer.produce(json, Topics.cmdFromHpcMaster);
        MainHandler_Master.cmdProducer.flush();
    }
}
