package SwitchAnalyzer.Commands.MOM.OnlyMOM;

import SwitchAnalyzer.Commands.ICommandMOM;
import SwitchAnalyzer.Database.DBSelect;
import SwitchAnalyzer.Database.Run_GUI;

import java.util.ArrayList;

public class GetFramesForRun_MOM implements ICommandMOM
{
    boolean injectErrors;
    ArrayList<Run_GUI> runs;

    @Override
    public void processCmd() {
        DBSelect.compareRuns(runs);
    }
}
