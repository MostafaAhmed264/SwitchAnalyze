package SwitchAnalyzer.Commands;

import SwitchAnalyzer.Collectors.MOMConsumer;

import SwitchAnalyzer.Collectors.MasterConsumer;
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

public class RetrieveCmd_Master extends ICommandMaster{

    public ArrayList<String> retrievals;

    public RetrieveCmd_Master(int portID, ArrayList<String> retrievals)
    {
        this.portID = portID;
        this.retrievals = retrievals;
    }

    @Override
    public void processCmd()
    {
        GlobalVariable.retrieveDataFromNode = true;
        for (MachineNode node : master.childNodes)
        {
            GenCmd(node.getMachineID());
        }
        addCollectors();
        openConsumeAndProduceThread();
    }



    private void addCollectors()
    {
        for (String key : retrievals) { MasterConsumer.addCollector(SystemMaps.collectors.get(key)); }
    }

    private void openConsumeAndProduceThread()
    {
        Thread dataConsumeAndProduceThread = new Thread (() ->
        {
            while(GlobalVariable.retrieveDataFromNode)
            {
                ProduceData_Master.produceData();
            }
        });
        dataConsumeAndProduceThread.start();
    }

    @Override
    public void GenCmd(int machineID)
    {
        String json = JSONConverter.toJSON(new RetrieveCmd_Node(machineID));
        json = "1" + json;
        MainHandler_Master.cmdProducer.produce(json, Topics.cmdFromHpcMaster);
        MainHandler_Master.cmdProducer.flush();
    }
}
