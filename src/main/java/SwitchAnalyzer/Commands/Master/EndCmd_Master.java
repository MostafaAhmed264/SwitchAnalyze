package SwitchAnalyzer.Commands.Master;

import SwitchAnalyzer.Collectors.MasterConsumer;
import SwitchAnalyzer.Commands.Node.EndRunCmd_Node;
import SwitchAnalyzer.Commands.ICommandMaster;
import SwitchAnalyzer.Kafka.Topics;
import SwitchAnalyzer.MainHandler_MOM;
import SwitchAnalyzer.miscellaneous.GlobalVariable;
import SwitchAnalyzer.miscellaneous.JSONConverter;
import SwitchAnalyzer.miscellaneous.SystemMaps;

public class EndCmd_Master extends ICommandMaster
{
    public EndCmd_Master(int id) { this.portID = id; }

    public void processCmd()
    {
        GlobalVariable.retrieveDataFromNode = false;
        GlobalVariable.stopRecieving = true; //CLOSES RECIEVING AS A WHOLE
        GenCmd(0);
        System.out.println("End cmd proccessing in master");
        clearMasterConsumer(); //PREPARING FOR NEW RUN MASTER SHOULD CLEAR STATIC FILEDS
    }

    public void clearMasterConsumer()
    {
        MasterConsumer.clearResults();
        MasterConsumer.clearCollectors();
    }

    public void GenCmd(int id)
    {
        String json = JSONConverter.toJSON(new EndRunCmd_Node(id));
        json = SystemMaps.END_RUN_CMD_NODE_IDX + json;
        MainHandler_MOM.cmdProducer.produce(json, Topics.cmdFromHpcMaster);
        MainHandler_MOM.cmdProducer.flush();
    }
}
