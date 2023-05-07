package SwitchAnalyzer.Database;

import SwitchAnalyzer.miscellaneous.JSONConverter;

import java.util.ArrayList;
import java.util.UUID;

public class HeadersOnly implements IStorage{
    public void store(Frame_DB frame)
    {
        HeadersFrame frameHeader = new HeadersFrame();
        frameHeader.port = frame.port;
        frameHeader.direction = frame.Direction;
        frameHeader.errorInRouting = frame.errorInRoutingExists();
        frameHeader.crcChecker = frame.errorInCrcCheckerExists();
        frameHeader.id = frame.getID();
        frameHeader.headers.addAll(frame.frameDetails.keySet());
        String json = JSONConverter.toJSON(frameHeader);
        DBInsert.insertFrame(json);
    }

    private class HeadersFrame
    {
        ArrayList<String> headers = new ArrayList<>();
        public UUID id ;
        public String port;
        public String direction;
        public boolean errorInRouting;
        public boolean crcChecker;
    }
}
