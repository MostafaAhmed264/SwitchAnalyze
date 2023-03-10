package SwitchAnalyzer.Commands;

import SwitchAnalyzer.Collectors.MasterConsumer;
import SwitchAnalyzer.Collectors.PLossCollectorMaster;
import SwitchAnalyzer.Collectors.RatesCollectorMaster;
import SwitchAnalyzer.Kafka.GenericConsumer;
import SwitchAnalyzer.Kafka.GenericProducer;
import SwitchAnalyzer.Kafka.Topics;
import SwitchAnalyzer.Machines.MachineNode;
import SwitchAnalyzer.MainHandler_Master;
import SwitchAnalyzer.Network.HardwareObjects.SwitchPort;
import SwitchAnalyzer.Network.HardwareObjects.SwitchPortConfig;
import SwitchAnalyzer.Network.IP;
import SwitchAnalyzer.Network.Ports;
import SwitchAnalyzer.miscellaneous.JSONConverter;
import SwitchAnalyzer.miscellaneous.Time;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static SwitchAnalyzer.MainHandler_Master.main;
import static SwitchAnalyzer.MainHandler_Master.master;

public class StartRunCommand_Master extends ICommandMaster
{
    public SwitchPortConfig portConfig;
    GenericProducer producer ;

    public StartRunCommand_Master(int portID , SwitchPortConfig portConfig)
    {
        this.portID = portID;
        this.portConfig = portConfig;
    }


    @Override
    public void processCmd()
    {
        producer = new GenericProducer(IP.ip1+":"+ Ports.port1);
        for (MachineNode node : master.childNodes)
        {
            GenCmd(node.getMachineID());
        }
        producer.close();
    }

    @Override
    public void GenCmd(int id)
    {
        StartRunCommand_Node command = new StartRunCommand_Node(portConfig , id);
        String json = JSONConverter.toJSON(command);
        System.out.println("StartRunCommand_Master: " + json);
        //dont forget to add number at the beginning of the json to indicate the type of the command
        json = "0"+json;
        producer.send(Topics.cmdFromHpcMaster, json);
    }
}
