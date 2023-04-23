package SwitchAnalyzer.Database;

import SwitchAnalyzer.miscellaneous.JSONConverter;
import com.datastax.driver.core.utils.UUIDs;

import java.util.ArrayList;
import java.util.UUID;

public class HeadersOnly implements IStorage{
    public void store(DBFrame frame)
    {
        HeadersFrame frameHeader = new HeadersFrame();
        frameHeader.sendingPort = frame.getSendingPort();
        frameHeader.recievingPort = frame.getRecievingPort();
        frameHeader.errorInRouting = frame.errorInRoutingExists();
        frameHeader.crcChecker = frame.errorInCrcCheckerExists();
        frameHeader.id = frame.getID();
        frameHeader.headers.addAll(frame.frameData.keySet());
        String json = JSONConverter.toJSON(frameHeader);
        DBInsert.insert(json);
    }

    private class HeadersFrame
    {
        ArrayList<String> headers = new ArrayList<>();
        public UUID id ;
        public int sendingPort;
        public int recievingPort;
        public boolean errorInRouting;
        public boolean crcChecker;
    }
}
