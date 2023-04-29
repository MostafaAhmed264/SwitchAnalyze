package SwitchAnalyzer.Commands.Master;

import SwitchAnalyzer.Collectors.MasterConsumer;
import SwitchAnalyzer.Commands.ICommandMaster;
import SwitchAnalyzer.Commands.Node.StartRunAllNodes;
import SwitchAnalyzer.Kafka.Topics;
import SwitchAnalyzer.MainHandler_Master;
import SwitchAnalyzer.Network.ErrorDetection.CRC;
import SwitchAnalyzer.Network.FrameProcessing;
import SwitchAnalyzer.ProduceData_Master;
import SwitchAnalyzer.miscellaneous.GlobalVariable;
import SwitchAnalyzer.miscellaneous.JSONConverter;
import SwitchAnalyzer.miscellaneous.SystemMaps;

public class StartRunALL extends ICommandMaster
{
    @Override
    /*
        Propagates the BROADCAST TO CHILDREN
        STARTS Frame PROCESSING
        STARTS COLLECTING FROM NODES
        STARTS PRODUCTION IN KAFKA
     */
    public void processCmd()
    {
        GenCmd(0);
        GlobalVariable.retrieveDataFromNode = true;
        addCollectors();
        openProcessFramesAndProduceDataThreads();
    }

    private void addCollectors()
    {
        MasterConsumer.initMasterCollectors();
    }

    private void openProcessFramesAndProduceDataThreads()
    {

        Thread t = new Thread (() ->
        {
            FrameProcessing.errorDetectingAlgorithms = new CRC();
            FrameProcessing.startProcessFrames();
        });
        t.start();

        Thread dataConsumeAndProduceThread = new Thread (() ->
        {
            while(GlobalVariable.retrieveDataFromNode) {
                ProduceData_Master.produceData();}
        });
        dataConsumeAndProduceThread.start();
    }
    @Override
    public void GenCmd(int machineID) {
        String json = JSONConverter.toJSON(new StartRunAllNodes(machineID));
        json = SystemMaps.START_RUN_ALL_NODE_IDX +json;
        MainHandler_Master.cmdProducer.produce(json, Topics.cmdFromHpcMaster);
        MainHandler_Master.cmdProducer.flush();
    }
}
