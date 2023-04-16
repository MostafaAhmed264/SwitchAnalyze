package SwitchAnalyzer;

import SwitchAnalyzer.Collectors.MOMConsumer;
import SwitchAnalyzer.Kafka.GenericConsumer;
import SwitchAnalyzer.Kafka.Topics;
import SwitchAnalyzer.Network.IP;
import SwitchAnalyzer.Network.Ports;
import SwitchAnalyzer.Sockets.JettyWebSocketServer;
import SwitchAnalyzer.UtilityExecution.FrameResult;
import SwitchAnalyzer.miscellaneous.GlobalVariable;
import SwitchAnalyzer.miscellaneous.JSONConverter;
import SwitchAnalyzer.miscellaneous.Time;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;

import java.util.ArrayList;


public class ProduceData_MOM
{
    static GenericConsumer consumer = new GenericConsumer(IP.ip1 + ":" + Ports.port1, "asdadsasfasbzx", true);
    public static void produceData(ArrayList<Integer> ids)
    {
        getFrames();
        MOMConsumer.updateHpcInfo();
        String json;
        for (int id : ids)
        {
            json = JSONConverter.toJSON(GlobalVariable.portHpcMap.get(id).hpcInfo);
            System.out.println("Before send" + json);
            try {
                JettyWebSocketServer.writeMessage(json);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        if (!MOMConsumer.getResults().isEmpty())
        {
            json = JSONConverter.toJSON(MOMConsumer.getResults());
            JettyWebSocketServer.writeMessage(json);
        }
    }

    public static void getFrames()
    {
        consumer.selectTopicByteArray(Topics.FramesFromHPC);
        ConsumerRecords<String, byte[]> records = consumer.consumeByteArray(Time.waitTime);
        for (ConsumerRecord<String, byte[]> record : records)
        {
            StringBuilder sb =new StringBuilder();
            for (int i = 0; i < record.value().length; i++)
            {
                sb.append(String.format("%02X", record.value()[i])).append(" ");
            }
            FrameResult frameResult = new FrameResult();
            frameResult.ID = 2;
            frameResult.s = sb.toString();
            String json = JSONConverter.toJSON(frameResult);
            System.out.println(json);
            JettyWebSocketServer.writeMessage(json);
        }
    }

}
