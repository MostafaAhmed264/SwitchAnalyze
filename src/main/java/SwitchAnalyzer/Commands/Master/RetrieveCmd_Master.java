package SwitchAnalyzer.Commands.Master;

import SwitchAnalyzer.Collectors.MOMConsumer;

import SwitchAnalyzer.Collectors.MasterConsumer;
import SwitchAnalyzer.Commands.ICommandMaster;
import SwitchAnalyzer.Kafka.Topics;
import SwitchAnalyzer.Machines.MachineNode;
import SwitchAnalyzer.MainHandler_Master;
import SwitchAnalyzer.Network.IP;
import SwitchAnalyzer.Network.Ports;
import SwitchAnalyzer.ProduceData_Master;
import SwitchAnalyzer.miscellaneous.GlobalVariable;
import SwitchAnalyzer.miscellaneous.JSONConverter;
import SwitchAnalyzer.miscellaneous.SystemMaps;

import java.util.ArrayList;

import static SwitchAnalyzer.MainHandler_Master.master;

public class RetrieveCmd_Master extends ICommandMaster {

    public ArrayList<String> retrievals;

    public RetrieveCmd_Master(int portID, ArrayList<String> retrievals)
    {
        this.portID = portID;
        this.retrievals = retrievals;
    }

    /*
        STARTS RETRIEVING PROCESSED FRAMES IN REALTIME THROUGH KAFKA
     */
    @Override
    public void processCmd()
    {
        GlobalVariable.retreiveProcessedFramesFromHPC = true;
    }

    @Override
    public void GenCmd(int machineID){}
}
