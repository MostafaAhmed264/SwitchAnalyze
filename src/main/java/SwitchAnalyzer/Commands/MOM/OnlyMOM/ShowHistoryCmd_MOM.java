package SwitchAnalyzer.Commands.MOM.OnlyMOM;

import SwitchAnalyzer.Commands.ICommandMOM;
import SwitchAnalyzer.Database.DBSelect;
import SwitchAnalyzer.Sockets.JettyWebSocketServer;

public class ShowHistoryCmd_MOM implements ICommandMOM
{

    @Override
    public void processCmd()
    {
        try {
            String json = DBSelect.showHistory();
            System.out.println(json);
            JettyWebSocketServer.writeMessage(json);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
