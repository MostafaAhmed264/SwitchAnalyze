package SwitchAnalyzer;

import SwitchAnalyzer.Collectors.Collector;
import SwitchAnalyzer.Collectors.PLossCollectorMaster;
import SwitchAnalyzer.Collectors.RatesCollectorMaster;
import SwitchAnalyzer.Commands.*;
import SwitchAnalyzer.Kafka.GenericConsumer;
import SwitchAnalyzer.Kafka.Topics;
import SwitchAnalyzer.Machines.MachineNode;
import SwitchAnalyzer.Machines.MasterOfHPC;
import SwitchAnalyzer.Network.*;
import SwitchAnalyzer.miscellaneous.GlobalVariable;
import SwitchAnalyzer.miscellaneous.JSONConverter;
import SwitchAnalyzer.miscellaneous.Time;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;

public class MainHandler_Master
{
    public static String consumerGroup = "command-consumer-group";
    public static ArrayList<Class<? extends ICommandMaster>> commandClasses = new ArrayList<>();
    public static ArrayList<Collector> collectors = new ArrayList<>();
    static GenericConsumer consumer;
    public static MasterOfHPC master;

    public static void init()
    {
//        read from config text file and construct HPC object from this config file
//        master = new MasterOfHPC(0);// needs to be adjusted by setting these values from the config file and setting it children nodes
//        master.childNodes.add(new MachineNode(0));
//        master.childNodes.add(new MachineNode(1));
//        GlobalVariable.portHpcMap.put(1, master);

        Logger logger = LoggerFactory.getLogger("MasterHPC");
        consumer = new GenericConsumer(IP.ip1 + ":" + Ports.port1, consumerGroup);
        consumer.selectTopic(Topics.cmdFromMOM);
        commandClasses.add(StartRunCommand_Master.class);
        commandClasses.add(RetrieveCmd_Master.class);
        collectors.add(new RatesCollectorMaster());
        collectors.add(new PLossCollectorMaster());
        //PCAP.initialize();
    }

    public static void start()
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
                System.out.println("MainHandlerMaster: "+json);
                ICommandMaster command = JSONConverter.fromJSON(json, commandClasses.get(commandTypeIndex));
                //we need to re check mapping ,how to make it global in all masters and MOM or what should we do ?!
                if (GlobalVariable.portHpcMap.get(command.portID).getHPCID() == master.getHPCID())
                {
                    Thread t1 = new Thread(() -> ProcessCmd.processCmd(command));
                    t1.start();
                }
            }
        }
    }

    //temp main for testing
    public static void main(String[] args){
        start();
    }
}


