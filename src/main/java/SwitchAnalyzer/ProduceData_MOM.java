package SwitchAnalyzer;

import SwitchAnalyzer.Collectors.MOMConsumer;
import SwitchAnalyzer.Sockets.JettyWebSocketServer;
import SwitchAnalyzer.Sockets.UserRequestHandler;
import SwitchAnalyzer.miscellaneous.GlobalVariable;
import SwitchAnalyzer.miscellaneous.JSONConverter;
import SwitchAnalyzer.miscellaneous.SystemMaps;

import java.util.ArrayList;

import static SwitchAnalyzer.MainHandler_MOM.masterOfMasters;

public class ProduceData_MOM
{
    public static void produceData(ArrayList<Integer> ids)
    {
        MOMConsumer.updateHpcInfo();
        String json;
        for (int id : ids)
        {
            json = JSONConverter.toJSON(GlobalVariable.portHpcMap.get(id).hpcInfo  3+);
            System.out.println("Before send" + json);
            JettyWebSocketServer.writeMessage(json);
        }
        if (!MOMConsumer.getResults().isEmpty())
        {
            json = JSONConverter.toJSON(MOMConsumer.getResults());
            JettyWebSocketServer.writeMessage(json);
        }
    }
}
