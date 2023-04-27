package SwitchAnalyzer.Commands;

import SwitchAnalyzer.Network.SendThreadsHandler;

public class ResumeRunCmd_Node extends ICommandNode
{
    ResumeRunCmd_Node(int id) { this.machineID = id; }
    public void processCmd() { SendThreadsHandler.resumeThreads(); }
}
