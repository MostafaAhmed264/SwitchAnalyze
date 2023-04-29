package SwitchAnalyzer.Commands.Node;

import SwitchAnalyzer.Commands.ICommandNode;
import SwitchAnalyzer.Network.SendThreadsHandler;

public class StopRunCmd_Node extends ICommandNode
{
    public StopRunCmd_Node(int machineID) { this.machineID = machineID; }

    public void processCmd() { SendThreadsHandler.stopThreads(); }
}
