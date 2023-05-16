


package SwitchAnalyzer;

import SwitchAnalyzer.ClusterConfigurations.ClusterConfiguartions;
import SwitchAnalyzer.ClusterConfigurations.MachineConfigurations;
import SwitchAnalyzer.ClusterConfigurations.MoMConfigurations;
import SwitchAnalyzer.Commands.Master.EndCmd_Master;
import SwitchAnalyzer.Commands.MOM.EndRunCmd_MOM;
import SwitchAnalyzer.Commands.Node.EndRunCmd_Node;
import SwitchAnalyzer.Kafka.GenericConsumer;
import SwitchAnalyzer.Kafka.Topics;
import SwitchAnalyzer.Machines.MachineNode;
import SwitchAnalyzer.Network.IP;
import SwitchAnalyzer.Network.Ports;
import SwitchAnalyzer.SystemInitializer.SystemInitializer;
import SwitchAnalyzer.miscellaneous.GlobalVariable;
import SwitchAnalyzer.miscellaneous.JSONConverter;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;

public class Main
{
    public static void start()
    {
        MachineNode myNode = new MachineNode();
        SystemInitializer.setConsumerGroups(myNode);

        GenericConsumer MainConsumer = new GenericConsumer(IP.ConfigurationsIP + ":" + Ports.port1, GlobalVariable.consumer0);
        MainConsumer.selectTopic(Topics.configurationsTopic);
        System.out.println("before while in main");
        while (true)

        {
            ConsumerRecords<String, String> records = MainConsumer.consume(100);
            for (ConsumerRecord<String, String> record : records)
            {
                System.out.println("in for");
                MoMConfigurations momConfigurations = JSONConverter.fromJSON(record.value(), MoMConfigurations.class);
                Thread HandlerThread;
                if (momConfigurations.getMaster_Mac().equals(myNode.nodeMacAddress))
                {
                    //new EndRunCmd_MOM().processCmd();
                    MainHandler_MOM.end();
                    SystemInitializer.MoMinit(myNode, momConfigurations);
                    HandlerThread = new Thread(MainHandler_MOM::start);
                    HandlerThread.start();
                    System.out.println("IN MAIN");
                }
                else
                {
                    for (ClusterConfiguartions clusterConfig : momConfigurations.cluster)
                    {
                        for (MachineConfigurations machineConfig : clusterConfig.machines)
                        {
                            if (machineConfig.getMac().equals(myNode.nodeMacAddress))
                            {
                                Topics.setTopicsNames(clusterConfig.getCluster_name());
                                if (machineConfig.Is_master())
                                {
                                    new EndCmd_Master(0).processCmd();
                                    MainHandler_Master.working = false;

                                    SystemInitializer.MasterInit(myNode, machineConfig, clusterConfig,momConfigurations);
                                    HandlerThread = new Thread(MainHandler_Master::start);
                                }
                                else
                                {
                                   // new EndRunCmd_Node(0).processCmd();
                                    MainHandler_Node.working = false;
                                    System.out.println("Node in main");
                                    SystemInitializer.NodeInit(myNode, machineConfig,momConfigurations);
                                    HandlerThread = new Thread(MainHandler_Node::start);
                                }
                                HandlerThread.start();
                            }
                        }
                    }
                }
            }
        }
    }
//
//    public static void start(String json)
//    {
//        MachineNode myNode = new MachineNode();
//        MoMConfigurations momConfigurations = JSONConverter.fromJSON(json, MoMConfigurations.class);
//        Thread HandlerThread;
//        if (momConfigurations.getMaster_Mac().equals(myNode.nodeMacAddress))
//        {
//            new EndRunCmd_MOM().processCmd();
//            MainHandler_MOM.end();
//            SystemInitializer.MoMinit(myNode, momConfigurations);
//            HandlerThread = new Thread(MainHandler_MOM::start);
//            HandlerThread.start();
//        }
//        else
//        {
//            for (ClusterConfiguartions clusterConfig : momConfigurations.cluster)
//            {
//                for (MachineConfigurations machineConfig : clusterConfig.machines)
//                {
//                    if (machineConfig.getMac().equals(myNode.nodeMacAddress))
//                    {
//                        if (machineConfig.Is_master())
//                        {
//                            new EndCmd_Master(0).processCmd();
//                            MainHandler_Master.working = false;
//                            SystemInitializer.MasterInit(myNode, machineConfig, clusterConfig,momConfigurations);
//                            HandlerThread = new Thread(MainHandler_Master::start);
//                        }
//                        else
//                        {
//                            new EndRunCmd_Node(0).processCmd();
//                            MainHandler_Node.working = false;
//                            SystemInitializer.NodeInit(myNode, machineConfig);
//                            HandlerThread = new Thread(MainHandler_Node::start);
//                        }
//                        HandlerThread.start();
//                    }
//                }
//            }
//        }
//    }
//

    public static void main(String[] args)
    {
        start();
    }
}
