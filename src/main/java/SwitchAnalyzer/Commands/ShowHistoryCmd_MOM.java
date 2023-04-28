package SwitchAnalyzer.Commands;

import SwitchAnalyzer.Database.DBSelect;
import SwitchAnalyzer.Sockets.JettyWebSocketServer;

public class ShowHistoryCmd_MOM implements ICommandMOM
{

    @Override
    public void processCmd()
    {
        try {
            JettyWebSocketServer.writeMessage(DBSelect.showHistory());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
