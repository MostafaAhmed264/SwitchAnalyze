package SwitchAnalyzer;

import SwitchAnalyzer.Collectors.MOMConsumer;
import SwitchAnalyzer.Database.DBFrame;
import SwitchAnalyzer.Kafka.GenericConsumer;
import SwitchAnalyzer.Kafka.Topics;
import SwitchAnalyzer.Machines.MasterOfHPC;
import SwitchAnalyzer.Network.IP;
import SwitchAnalyzer.Network.Ports;
import SwitchAnalyzer.Sockets.JettyWebSocketServer;
import SwitchAnalyzer.Network.FrameResult;
import SwitchAnalyzer.miscellaneous.GlobalVariable;
import SwitchAnalyzer.miscellaneous.JSONConverter;
import SwitchAnalyzer.miscellaneous.SystemMaps;
import SwitchAnalyzer.miscellaneous.Time;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;

import java.util.ArrayList;


public class ProduceData_MOM
{
    static GenericConsumer consumer = new GenericConsumer(IP.ip1 + ":" + Ports.port1, "frame_Consumer3v2e3342");
    public static void produceData(ArrayList<Integer> ids)
    {
        String json;

        if (ids.get(0) == 0)
        {
            for (MasterOfHPC master : MainHandler_MOM.masterOfMasters.HPCs)
            {
                json = JSONConverter.toJSON(master.hpcInfo);
                System.out.println("Before send" + json);
                try { JettyWebSocketServer.writeMessage(json); }
                catch (Exception e) { throw new RuntimeException(e); }
            }
        }
        else
        {
            getFrames();
            for (int id : ids)
            {
                json = JSONConverter.toJSON(GlobalVariable.portHpcMap.get(id).hpcInfo);
                System.out.println("Before send" + json);
                try { JettyWebSocketServer.writeMessage(json); }
                catch (Exception e) { throw new RuntimeException(e); }
            }
        }
    }

    public static void getFrames()
    {
        consumer.selectTopic(Topics.ProcessedFramesFromHPC);
        ConsumerRecords<String, String> records = consumer.consume(Time.waitTime);
        for (ConsumerRecord<String, String> record : records)
        {
            DBFrame f = JSONConverter.fromJSON(record.value(), DBFrame.class);
            f.json = "inspect";
            String json = JSONConverter.toJSON(f);
            System.out.println(json);
            JettyWebSocketServer.writeMessage(json);
        }
    }
}
