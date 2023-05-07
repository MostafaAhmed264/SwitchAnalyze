package SwitchAnalyzer.Database;

import java.util.ArrayList;

public class Switch_GUI extends Switch
{
    public String totalNoOfPorts;
    public ArrayList<Run_GUI> stats;
    public Switch_GUI(String switchName, String totalNoOfPorts) {
        super(switchName);
        this.totalNoOfPorts = totalNoOfPorts;
    }
}
