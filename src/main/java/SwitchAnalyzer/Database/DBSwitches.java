package SwitchAnalyzer.Database;

import java.util.ArrayList;

public class DBSwitches
{
    ArrayList<DBSwitch> dbSwitches = new ArrayList<>();

    public DBSwitches(ArrayList<DBSwitch> dbSwitches) {
        this.dbSwitches = dbSwitches;
    }

    public ArrayList<DBSwitch> getDbSwitches() {
        return dbSwitches;
    }

    public void setDbSwitches(ArrayList<DBSwitch> dbSwitches) {
        this.dbSwitches = dbSwitches;
    }
}
