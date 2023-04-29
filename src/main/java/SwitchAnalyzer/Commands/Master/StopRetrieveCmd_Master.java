package SwitchAnalyzer.Commands.Master;

import SwitchAnalyzer.Collectors.MasterConsumer;
import SwitchAnalyzer.Commands.ICommandMaster;
import SwitchAnalyzer.Kafka.Topics;
import SwitchAnalyzer.Machines.MachineNode;
import SwitchAnalyzer.MainHandler_Master;
import SwitchAnalyzer.miscellaneous.GlobalVariable;
import SwitchAnalyzer.miscellaneous.JSONConverter;
import static SwitchAnalyzer.MainHandler_Master.master;

public class StopRetrieveCmd_Master extends ICommandMaster
{

    public StopRetrieveCmd_Master(int portID)
    {
        this.portID = portID;
    }

    /*
        Stops RETRIEVING FRAMES
     */
    public void processCmd()
    {
        //FLAG FOR STOPPING PRODUCING FRAMES IN KAFKA
        GlobalVariable.retreiveProcessedFramesFromHPC = false;
    }

    public void GenCmd(int machineID)
    {}

}
