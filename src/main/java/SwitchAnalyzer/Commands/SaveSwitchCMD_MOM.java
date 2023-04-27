package SwitchAnalyzer.Commands;

import SwitchAnalyzer.Database.DBConnect;
import SwitchAnalyzer.Database.DBSwitch;
import SwitchAnalyzer.miscellaneous.GlobalVariable;

public class SaveSwitchCMD_MOM implements ICommand
{
    public String switchName;
    public int portNum;
    public void processCmd()
    {
        DBConnect.startRun(new DBSwitch(switchName, portNum));
        GlobalVariable.switchName = switchName;
    }
}
