package SwitchAnalyzer.Commands;

import SwitchAnalyzer.Collectors.MasterConsumer;
import SwitchAnalyzer.Kafka.GenericProducer;
import SwitchAnalyzer.Kafka.Topics;
import SwitchAnalyzer.Machines.MachineNode;
import SwitchAnalyzer.MainHandler_Master;
import SwitchAnalyzer.Network.HardwareObjects.SwitchPort;
import SwitchAnalyzer.Network.IP;
import SwitchAnalyzer.Network.Ports;
import SwitchAnalyzer.ProduceData_MOM;
import SwitchAnalyzer.ProduceData_Master;
import SwitchAnalyzer.miscellaneous.GlobalVariable;
import SwitchAnalyzer.miscellaneous.JSONConverter;

import java.util.ArrayList;

import static SwitchAnalyzer.MainHandler_Master.master;

public class RetrieveCmd_MOM implements ICommandMOM
{
    public static Thread listeningThread;
    ArrayList<SwitchPort> ports= new ArrayList<>();

    @Override
    public void processCmd()
    {
        GlobalVariable.retrieveDataFromNode = true;

        for (SwitchPort port : ports)
        {
            GenCmd(port);
        }

        MasterConsumer.addCollector(MainHandler_Master.collectors.get(0));
        MasterConsumer.addCollector(MainHandler_Master.collectors.get(1));
        listeningThread = new Thread (() ->
        {
            while(GlobalVariable.retrieveDataFromNode)
            {
                ProduceData_MOM.produceData();
            }
        });
    }

    @Override
    public void GenCmd(SwitchPort port)
    {
        RetrieveCmd_Master command = new RetrieveCmd_Master(port.ID);
        String json = JSONConverter.toJSON(command);
        System.out.println("RetrieveCmd_MOM: "+ json);
        //dont forget to add number at the beginning of the json to indicate the type of the command
        json = "1" + json;
        GenericProducer producer = new GenericProducer(IP.ip1+":"+ Ports.port1);
        producer.send(Topics.cmdFromMOM, json);
        producer.close();
    }
}
