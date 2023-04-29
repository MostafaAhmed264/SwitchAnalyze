package SwitchAnalyzer.Commands.Node;

import SwitchAnalyzer.Commands.ICommandNode;
import SwitchAnalyzer.Network.SendThreadsHandler;

public class ResumeRunCmd_Node extends ICommandNode
{
    public ResumeRunCmd_Node(int id) { this.machineID = id; }
    public void processCmd() { SendThreadsHandler.resumeThreads(); }
}
