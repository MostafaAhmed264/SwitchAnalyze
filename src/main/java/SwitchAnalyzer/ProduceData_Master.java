package SwitchAnalyzer;
import SwitchAnalyzer.Collectors.MasterConsumer;
import SwitchAnalyzer.Kafka.Topics;
import SwitchAnalyzer.miscellaneous.JSONConverter;
import static SwitchAnalyzer.MainHandler_Master.master;

/**
 * this class is responsible for sending the metrics of HPC to kafka
 */

public class ProduceData_Master
{
    public static void produceData()
    {
        master.hpcInfo.map = MasterConsumer.consume();
        String json = JSONConverter.toJSON(master.hpcInfo);
        MainHandler_Master.dataProducer.produce(json,Topics.ratesFromHPCs);
        MainHandler_Master.dataProducer.flush();
    }
}
