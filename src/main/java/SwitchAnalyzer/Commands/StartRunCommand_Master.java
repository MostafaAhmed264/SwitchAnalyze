package SwitchAnalyzer.Commands;

import SwitchAnalyzer.Database.DBConnect;
import SwitchAnalyzer.Kafka.Topics;
import SwitchAnalyzer.Machines.MachineNode;
import SwitchAnalyzer.MainHandler_Master;
import SwitchAnalyzer.Network.HardwareObjects.SwitchPortPair;
import SwitchAnalyzer.miscellaneous.GlobalVariable;
import SwitchAnalyzer.miscellaneous.JSONConverter;

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

    public StartRunCommand_Master(SwitchPortPair portPair, int saveOption, String switchName)
    {
        this.portPair = portPair ;
        this.portID = portPair.fromPort.ID;
        GlobalVariable.storageClass = saveOption;
        this.switchName = switchName;
    }

    @Override
    public void processCmd()
    {
        //DBConnect.connectToDB_Node(switchName);
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
