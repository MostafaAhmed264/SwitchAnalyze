package SwitchAnalyzer;

import SwitchAnalyzer.Commands.ICommandMaster;
import SwitchAnalyzer.Commands.ProcessCmd;
import SwitchAnalyzer.Database.HeadersOnly;
import SwitchAnalyzer.Database.Headers_Data;
import SwitchAnalyzer.Database.IStorage;
import SwitchAnalyzer.Database.NoStore;
import SwitchAnalyzer.Kafka.GenericConsumer;
import SwitchAnalyzer.Kafka.Producer;
import SwitchAnalyzer.Kafka.Topics;
import SwitchAnalyzer.Machines.MasterOfHPC;
import SwitchAnalyzer.Network.IP;
import SwitchAnalyzer.Network.Ports;
import SwitchAnalyzer.miscellaneous.GlobalVariable;
import SwitchAnalyzer.miscellaneous.JSONConverter;
import SwitchAnalyzer.miscellaneous.SystemMaps;
import SwitchAnalyzer.miscellaneous.Time;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;

import java.util.ArrayList;
import java.util.Arrays;

public class MainHandler_Master
{
    public static boolean working = false;
    public static String consumerGroup = "dsifuhdsuf,mjkk";
    public static Producer cmdProducer = new Producer(IP.ip1);
    public static Producer dataProducer = new Producer(IP.ip1);
    static GenericConsumer consumer;
    public static MasterOfHPC master;
    public static ArrayList<IStorage> storages = new ArrayList<>(Arrays.asList(new NoStore() , new HeadersOnly(), new Headers_Data()));

    public static void init()
    {
        SystemMaps.initMapsMaster();
        consumer = new GenericConsumer(IP.ip1 + ":" + Ports.port1, consumerGroup);
        consumer.selectTopic(Topics.cmdFromMOM);
    }

    public static void end()
    {
        System.out.println("here in Master end ");
        working = false;
    }

    public static void start(){
        working = true;
        init();
        int commandTypeIndex;
        while (true)
        {
            System.out.println("here in the start of the master");
            ConsumerRecords<String, String> records = consumer.consume(Time.waitTime);
            System.out.println("master hpcid "+master.getHPCID()+" hpc map for port "+GlobalVariable.portHpcMap.get(1).getHPCID() );
            System.out.println("master id "+ master.getHPCID());
            for (ConsumerRecord<String, String> record : records)
            {

                String json = record.value();
                System.out.println("the json "+json);
                commandTypeIndex = Character.getNumericValue(json.charAt(0));
                json = json.replaceFirst("[0-9]*",""); //removing the number indicating the command type using regex
                ICommandMaster command = JSONConverter.fromJSON(json, SystemMaps.commandClassesMaster.get(commandTypeIndex));

                if (command.portID == 0 || GlobalVariable.portHpcMap.get(command.portID).getHPCID() == master.getHPCID())
                {
                    System.out.println("master hpcid "+master.getHPCID()+" hpc map for port "+GlobalVariable.portHpcMap.get(1).getHPCID() );
                    System.out.println("master id "+ master.getHPCID());
                    Thread t1 = new Thread(() -> ProcessCmd.processCmd(command));
                    t1.start();
                }
            }
        }
    }
    /*
    public static void main(String[] args)
    {
        working = true;
        init();
        int commandTypeIndex;
        while (true)
        {
            ConsumerRecords<String, String> records = consumer.consume(Time.waitTime);
            for (ConsumerRecord<String, String> record : records)
            {

                String json = record.value();
                System.out.println(json);
                commandTypeIndex = Character.getNumericValue(json.charAt(0));
                json = json.replaceFirst("[0-9]*",""); //removing the number indicating the command type using regex
                ICommandMaster command = JSONConverter.fromJSON(json, SystemMaps.commandClassesMaster.get(commandTypeIndex));
                if (command.portID == 0 || GlobalVariable.portHpcMap.get(command.portID).getHPCID() == master.getHPCID())
                {
                    Thread t1 = new Thread(() -> ProcessCmd.processCmd(command));
                    t1.start();
                }
            }
        }
    }

     */
}


