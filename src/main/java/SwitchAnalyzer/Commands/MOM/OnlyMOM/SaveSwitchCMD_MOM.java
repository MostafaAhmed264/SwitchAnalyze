package SwitchAnalyzer.Commands.MOM.OnlyMOM;

import SwitchAnalyzer.Commands.ICommand;
import SwitchAnalyzer.Database.DBConnect;
import SwitchAnalyzer.Database.Switch_DB;
import SwitchAnalyzer.miscellaneous.GlobalVariable;

public class SaveSwitchCMD_MOM implements ICommand
{
    public String switchName;
    public Long portNum;
    public void processCmd()
    {
        DBConnect.startRun(new Switch_DB(switchName, portNum));
        GlobalVariable.switchName = switchName;
    }
}
