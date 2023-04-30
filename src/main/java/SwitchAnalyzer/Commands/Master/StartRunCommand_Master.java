package SwitchAnalyzer.Commands.Master;

import SwitchAnalyzer.Collectors.MasterConsumer;
import SwitchAnalyzer.Commands.ICommandMaster;
import SwitchAnalyzer.Commands.Node.StartRunCommand_Node;
import SwitchAnalyzer.Database.DBConnect;
import SwitchAnalyzer.Kafka.Topics;
import SwitchAnalyzer.Machines.MachineNode;
import SwitchAnalyzer.MainHandler_Master;
import SwitchAnalyzer.Network.ErrorDetection.CRC;
import SwitchAnalyzer.Network.FrameProcessing;
import SwitchAnalyzer.Network.HardwareObjects.SwitchPortPair;
import SwitchAnalyzer.ProduceData_Master;
import SwitchAnalyzer.miscellaneous.GlobalVariable;
import SwitchAnalyzer.miscellaneous.JSONConverter;
import SwitchAnalyzer.miscellaneous.SystemMaps;

import static SwitchAnalyzer.MainHandler_Master.master;

public class StartRunCommand_Master extends ICommandMaster
{
    public SwitchPortPair portPair;
    int saveOption;
    String switchName;

    public StartRunCommand_Master(SwitchPortPair portPair)
    {
        this.portPair = portPair ;
        this.portID = portPair.fromPort.ID;
    }

    /*
        Propagates The Sending CONFIG TO THE NODES SO THEY CAN START SENDING
        SO MACHINES OPEN SENDING THREADS
     */
    @Override
    public void processCmd()
    {
        System.out.println(DBConnect.getSession());
        for (MachineNode node : master.childNodes)
        {
            GenCmd(node.getMachineID());
        }
    }

    /*
        IF THE START RUN WAS BROADCAST JUST BROADCAST TO NODES SO THE WON'T SEND
     */
    @Override
    public void GenCmd(int id)
    {
        String json = JSONConverter.toJSON(new StartRunCommand_Node(portPair.fromPort.portConfig, id ,portPair.toPort));
        json = SystemMaps.START_RUN_NODE_IDX +json;
        MainHandler_Master.cmdProducer.produce(json,Topics.cmdFromHpcMaster);
        MainHandler_Master.cmdProducer.flush();
    }
}
