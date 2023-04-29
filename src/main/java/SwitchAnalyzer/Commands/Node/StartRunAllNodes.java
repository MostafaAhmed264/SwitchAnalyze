package SwitchAnalyzer.Commands.Node;

import SwitchAnalyzer.Commands.ICommandNode;
import SwitchAnalyzer.Network.FrameReciever;
import SwitchAnalyzer.ProduceData_Node;
import SwitchAnalyzer.UtilityExecution.UtilityExecutor;
import SwitchAnalyzer.miscellaneous.SystemMaps;

public class StartRunAllNodes extends ICommandNode
{
    /*
         OPENS MONITORS
         OPENS PRODUCTION IN KAFKA FOR FRAMES IN BYTES
         OPENS PRODUCTION IN KAFKA FOR MONITOR RESULTS
      */

    public StartRunAllNodes(int machineID) { this.machineID = machineID; }

    public void processCmd()
    {
        addUtils();
        FrameReciever.startRec();
        openProduceThread();
    }

    public void addUtils()
    {
        UtilityExecutor.executors.addAll(SystemMaps.executorHashMap.values());
    }

    public void openProduceThread()
    {
        Thread executeUtilitiesThread = new Thread (() ->
        {
            while(!UtilityExecutor.executors.isEmpty()) {ProduceData_Node.produceData();}
        });
        if(!UtilityExecutor.executors.isEmpty()) { executeUtilitiesThread.start(); }
    }
}
