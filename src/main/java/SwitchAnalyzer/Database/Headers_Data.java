package SwitchAnalyzer.Database;

import SwitchAnalyzer.miscellaneous.JSONConverter;

public class Headers_Data implements IStorage{
    public void store(DBFrame dbFrame)
    {
        String json = JSONConverter.toJSON(dbFrame);
        System.out.println(json);
        DBInsert.insertFrame(json);
    }
}
