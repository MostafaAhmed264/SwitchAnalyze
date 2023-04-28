package SwitchAnalyzer.Commands;

import SwitchAnalyzer.Database.DBConnect;
import SwitchAnalyzer.Database.DBInsert;
import SwitchAnalyzer.Kafka.Topics;
import SwitchAnalyzer.Machines.MachineNode;
import SwitchAnalyzer.MainHandler_Master;
import SwitchAnalyzer.Network.ErrorDetection.ErrorDetectingAlgorithms;
import SwitchAnalyzer.Network.FrameProcessing;
import SwitchAnalyzer.Network.PacketInfo;
import SwitchAnalyzer.miscellaneous.JSONConverter;

import static SwitchAnalyzer.MainHandler_Master.master;

public class StartRecieve_Master  extends ICommandMaster
{
    public static ErrorDetectingAlgorithms errorDetectingAlgorithm;
    StartRecieve_Master(){ errorDetectingAlgorithm = null; this.portID = 0; }
    public void processCmd()
    {
        openProcessThread();
        for (MachineNode node : master.childNodes) { GenCmd(node.getMachineID()); }
    }
    private void openProcessThread()
    {
        Thread t = new Thread (() -> {
            FrameProcessing.errorDetectingAlgorithms = this.errorDetectingAlgorithm;
            FrameProcessing.startProcessFrames();
        });
        t.start();
    }

    public void GenCmd(int machineID)
    {
        String json = JSONConverter.toJSON(new StartRecieve_Node(machineID));
        json = "5"+json;
        MainHandler_Master.cmdProducer.produce(json, Topics.cmdFromHpcMaster);
        MainHandler_Master.cmdProducer.flush();
    }

}
