package SwitchAnalyzer;

import SwitchAnalyzer.Kafka.Topics;
import SwitchAnalyzer.UtilityExecution.UtilityExecutor;
import SwitchAnalyzer.miscellaneous.GlobalVariable;
import SwitchAnalyzer.miscellaneous.JSONConverter;

/**
 * this class is responsible for sending the rates to kafka
 * so that the master could collect them and send them to the MOM
 */
public class ProduceData_Node
{
    public static void produceData()
    {
        UtilityExecutor.executeUtils();
        MainHandler_Node.node.machineInfo.map = UtilityExecutor.result;
        if(GlobalVariable.retrieveDataFromNode)
        {
            try
            {
                String json = JSONConverter.toJSON(MainHandler_Node.node.machineInfo);
                MainHandler_Node.dataProducer.produce(json, Topics.ratesFromMachines);
                MainHandler_Node.dataProducer.flush();
            }
            catch (Exception e) { e.printStackTrace(); }
        }
    }
}
