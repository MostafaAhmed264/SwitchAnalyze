package SwitchAnalyzer.Commands;

import SwitchAnalyzer.Network.FrameReciever;

public class StartRecieve_Node extends ICommandNode
{
    public StartRecieve_Node(int machineID) { this.machineID = machineID; }
    public void processCmd()
    {
        FrameReciever.startRec();
    }
}
