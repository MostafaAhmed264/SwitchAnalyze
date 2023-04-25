package SwitchAnalyzer;


import SwitchAnalyzer.ClusterConfigurations.ClusterConfiguartions;
import SwitchAnalyzer.ClusterConfigurations.MachineConfigurations;
import SwitchAnalyzer.ClusterConfigurations.MoMConfigurations;
import SwitchAnalyzer.Kafka.GenericConsumer;

import SwitchAnalyzer.Kafka.Topics;
import SwitchAnalyzer.Machines.MOM;
import SwitchAnalyzer.Machines.MachineNode;

import SwitchAnalyzer.Machines.MasterOfHPC;
import SwitchAnalyzer.Network.IP;
import SwitchAnalyzer.Network.Ports;
import SwitchAnalyzer.SystemInitializer.SystemInitializer;
import SwitchAnalyzer.miscellaneous.GlobalVariable;
import SwitchAnalyzer.miscellaneous.JSONConverter;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;

import java.util.ArrayList;

public class Main {

    static String consumerConfigurationGroup = "G";

    public static void main(String[] args) {
        MachineNode myNode = new MachineNode();
        GenericConsumer MainConsumer = new GenericConsumer(IP.ConfigurationsIP + ":" + Ports.port1, consumerConfigurationGroup);
        MainConsumer.selectTopic(Topics.configurations);

        while (true) {
            ConsumerRecords<String, String> records = MainConsumer.consume(100);

            for (ConsumerRecord<String, String> record : records) {
                MoMConfigurations momConfigurations = JSONConverter.fromJSON(record.value(), MoMConfigurations.class);

                Thread HandlerThread;

                if (momConfigurations.getMaster_Mac().equals(myNode.nodeMacAddress)) {
                    SystemInitializer.MoMinit(myNode, momConfigurations);
                    HandlerThread = new Thread(MainHandler_MOM::start);
                    HandlerThread.start();

                } else {

                    for (ClusterConfiguartions clusterConfig : momConfigurations.cluster) {

                        for (MachineConfigurations machineConfig : clusterConfig.machines) {

                            if (machineConfig.getMac().equals(myNode.nodeMacAddress)) {

                                if (machineConfig.Is_master()) {
                                    SystemInitializer.MasterInit(myNode, machineConfig, clusterConfig);
                                    HandlerThread = new Thread(MainHandler_Master::start);
                                    HandlerThread.start();
                                } else {
                                    SystemInitializer.NodeInit(myNode, machineConfig);
                                    HandlerThread = new Thread(MainHandler_Node::start);
                                    HandlerThread.start();
                                }
                            }
                        }

                    }
                }
            }


        }
    }

}
