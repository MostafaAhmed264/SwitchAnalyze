package SwitchAnalyzer.Database;

import SwitchAnalyzer.miscellaneous.JSONConverter;

public class Headers_Data implements IStorage{
    public void store(Frame_DB dbFrame)
    {
        String json = JSONConverter.toJSON(dbFrame);
        System.out.println(json);
        DBInsert.insertFrame(json);
    }
}
