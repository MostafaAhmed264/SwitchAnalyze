package SwitchAnalyzer.Database;

import SwitchAnalyzer.miscellaneous.JSONConverter;

public class Headers_Data implements IStorage{
    public void store(DBFrame dbFrame)
    {
        String json = JSONConverter.toJSON(dbFrame);
        DBInsert.insertFrameJson(json);
    }
}
