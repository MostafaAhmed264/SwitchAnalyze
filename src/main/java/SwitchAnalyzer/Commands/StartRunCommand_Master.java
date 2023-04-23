package SwitchAnalyzer.Commands;

import SwitchAnalyzer.Kafka.Topics;
import SwitchAnalyzer.Machines.MachineNode;
import SwitchAnalyzer.MainHandler_Master;
import SwitchAnalyzer.Network.FrameProcessing;
import SwitchAnalyzer.Network.HardwareObjects.SwitchPortPair;
import SwitchAnalyzer.miscellaneous.JSONConverter;

import static SwitchAnalyzer.MainHandler_Master.master;

public class StartRunCommand_Master extends ICommandMaster
{
    public SwitchPortPair portPair;

    public StartRunCommand_Master(SwitchPortPair portPair)
    {
        this.portPair = portPair ;
        this.portID = portPair.fromPort.ID;
    }

    @Override
    public void processCmd()
    {
        for (MachineNode node : master.childNodes)
        {
            GenCmd(node.getMachineID());
        }
    }


    @Override
    public void GenCmd(int id)
    {
        String json = JSONConverter.toJSON(new StartRunCommand_Node(portPair.fromPort.portConfig, id ,portPair.toPort));
        json = "0"+json;
        MainHandler_Master.cmdProducer.produce(json,Topics.cmdFromHpcMaster);
        MainHandler_Master.cmdProducer.flush();
    }
}
