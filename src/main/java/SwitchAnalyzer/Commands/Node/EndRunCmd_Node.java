package SwitchAnalyzer.Commands.Node;

import SwitchAnalyzer.Commands.ICommandNode;
import SwitchAnalyzer.Network.FrameReciever;
import SwitchAnalyzer.UtilityExecution.UtilityExecutor;
import SwitchAnalyzer.miscellaneous.GlobalVariable;

public class EndRunCmd_Node extends ICommandNode
{
    public EndRunCmd_Node(int id)
    {
        this.machineID = id ;
    }
    public void processCmd()
    {
        FrameReciever.endRec();
        GlobalVariable.stopSending = true;
        UtilityExecutor.clearUtils(); //CLOSES TWO THINGS MONITORS AND PRODUCTION
    }
}
