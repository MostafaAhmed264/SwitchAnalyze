package SwitchAnalyzer.Commands;

import SwitchAnalyzer.miscellaneous.GlobalVariable;

public class EndRunCmd_Node extends ICommandNode
{
    public EndRunCmd_Node(int id)
    {
        this.machineID = id ;
    }
    public void processCmd()
    {
        GlobalVariable.stopRecieving = true;
        GlobalVariable.stopSending = true;
        GlobalVariable.retrieveDataFromNode = false;
    }
}
