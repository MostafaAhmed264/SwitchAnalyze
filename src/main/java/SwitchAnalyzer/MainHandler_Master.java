package SwitchAnalyzer;

import SwitchAnalyzer.Commands.ICommandMaster;
import SwitchAnalyzer.Commands.ProcessCmd;
import SwitchAnalyzer.Kafka.GenericConsumer;
import SwitchAnalyzer.Kafka.Producer;
import SwitchAnalyzer.Kafka.Topics;
import SwitchAnalyzer.Machines.MachineNode;
import SwitchAnalyzer.Machines.MasterOfHPC;
import SwitchAnalyzer.Network.IP;
import SwitchAnalyzer.Network.PCAP;
import SwitchAnalyzer.Network.Ports;
import SwitchAnalyzer.miscellaneous.GlobalVariable;
import SwitchAnalyzer.miscellaneous.JSONConverter;
import SwitchAnalyzer.miscellaneous.SystemMaps;
import SwitchAnalyzer.miscellaneous.Time;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;

public class MainHandler_Master
{
    public static String consumerGroup = "cgommdsandq-cosnsumer-gdhshdsffsdhbcxvncbmrouasdybbbbbbbbbbbbbbbbbbtuydtjuyp12";
    public static Producer cmdProducer = new Producer(IP.ip1);
    public static Producer dataProducer = new Producer(IP.ip1);
    static GenericConsumer consumer;
    public static MasterOfHPC master;

    public static void init()
    {
        SystemMaps.initMapsMaster();
        consumer = new GenericConsumer(IP.ip1 + ":" + Ports.port1, consumerGroup);
        consumer.selectTopic(Topics.cmdFromMOM);
    }

    public static void main(String[] args)
    {
        init();
        int commandTypeIndex;
        while (true)
        {
            ConsumerRecords<String, String> records = consumer.consume(Time.waitTime);
            for (ConsumerRecord<String, String> record : records)
            {
                String json = record.value();
                commandTypeIndex = Character.getNumericValue(json.charAt(0));
                json = json.replaceFirst("[0-9]*",""); //removing the number indicating the command type using regex
                ICommandMaster command = JSONConverter.fromJSON(json, SystemMaps.commandClassesMaster.get(commandTypeIndex));
                System.out.println(command.portID);
                if (GlobalVariable.portHpcMap.get(command.portID).getHPCID() == master.getHPCID())
                {
                    Thread t1 = new Thread(() -> ProcessCmd.processCmd(command));
                    t1.start();
                }
            }
        }
    }
}


