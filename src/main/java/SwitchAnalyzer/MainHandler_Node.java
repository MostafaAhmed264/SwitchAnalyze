package SwitchAnalyzer;

import SwitchAnalyzer.Commands.ICommandNode;
import SwitchAnalyzer.Commands.ProcessCmd;
import SwitchAnalyzer.Kafka.GenericConsumer;
import SwitchAnalyzer.Kafka.GenericProducer;
import SwitchAnalyzer.Kafka.Producer;
import SwitchAnalyzer.Kafka.Topics;
import SwitchAnalyzer.Machines.MachineNode;
import SwitchAnalyzer.Network.*;
import SwitchAnalyzer.miscellaneous.GlobalVariable;
import SwitchAnalyzer.miscellaneous.JSONConverter;
import SwitchAnalyzer.miscellaneous.SystemMaps;
import SwitchAnalyzer.miscellaneous.Time;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.pcap4j.core.PcapNetworkInterface;
import org.pcap4j.core.Pcaps;

import java.util.List;


public class MainHandler_Node
{
    public static boolean working = false;
    public static String consumerGroup = GlobalVariable.consumer2;
    static GenericConsumer consumer;
    public static MachineNode node;
    public static Producer dataProducer = new Producer(IP.ip1);
    public static GenericProducer packetProducer = new GenericProducer(IP.ip1, true);

    public static void init()
    {
        try
        {
            List<PcapNetworkInterface> allDevs = Pcaps.findAllDevs();
           PcapNetworkInterface nif = allDevs.get(0);
           GlobalVariable.interfaceName = nif.getName();
        }
        catch (Exception ex) { System.out.println("Problem in setting pcap network device in node init"); }
       // SystemMaps.nodeInitStub();
        SystemMaps.initMapsNode();
        consumer = new GenericConsumer(IP.ip1 + ":" + Ports.port1, consumerGroup);
        consumer.selectTopic(Topics.cmdFromHpcMaster);
//        PCAP.initialize();
    }

    public static void end() { working = false; }

    public static void start()
    {
        System.out.println("In node main handler");
        working = true;
        init();
        int commandTypeIndex;
        while (working)
        {
            ConsumerRecords<String, String> records = consumer.consume(Time.waitTime);
            for (ConsumerRecord<String, String> record : records)
            {
                String json = record.value();
                System.out.println("MainHandlerNode: "+ json);
                commandTypeIndex = Character.getNumericValue(json.charAt(0));
                json = json.replaceFirst("[0-9]*",""); //removing the number indicating the command type using regex
                ICommandNode command = JSONConverter.fromJSON(json, SystemMaps.commandClassesNode.get(commandTypeIndex));
                //we need to re check mapping ,how to make it global in all masters and MOM or what should we do ?
                if (command.machineID == 0 || command.machineID == node.getMachineID())
                {
                    Thread t1 = new Thread(() -> ProcessCmd.processCmd(command));
                    t1.start();
                }
            }
        }
        SystemMaps.clear();
    }
    public static void main(String[] args)
    {
        init();
        start();
    }

}
