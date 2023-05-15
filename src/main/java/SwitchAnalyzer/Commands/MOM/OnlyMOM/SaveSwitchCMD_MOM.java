package SwitchAnalyzer.Commands.MOM.OnlyMOM;

import SwitchAnalyzer.Commands.ICommand;
import SwitchAnalyzer.Database.DBConnect;
import SwitchAnalyzer.Database.DBSwitch;
import SwitchAnalyzer.miscellaneous.GlobalVariable;

public class SaveSwitchCMD_MOM implements ICommand
{
    public String switchName;
    public String switchPorts;
    public void processCmd()
    {
        DBConnect.startRun(new DBSwitch(switchName, Long.parseLong(switchPorts)));
        GlobalVariable.switchName = switchName;
    }
}
