package SwitchAnalyzer;

import SwitchAnalyzer.Commands.*;
import SwitchAnalyzer.Commands.ICommand;
import SwitchAnalyzer.Database.DBConnect;
import SwitchAnalyzer.Kafka.Producer;
import SwitchAnalyzer.Machines.MOM;
import SwitchAnalyzer.Network.IP;
import SwitchAnalyzer.Network.Ports;
import SwitchAnalyzer.Sockets.JettyWebSocketServer;
import SwitchAnalyzer.Sockets.WebSocketServer;
import SwitchAnalyzer.miscellaneous.SystemMaps;


import java.util.LinkedList;
import java.util.Queue;

public class MainHandler_MOM
{
    public static WebSocketServer server;
    public static Producer cmdProducer = new Producer(IP.ip1);
    static volatile int x;
    public static Queue<ICommand> commands = new LinkedList<>();
    public static volatile MOM masterOfMasters;

    public static void init()
    {
        DBConnect.connect();
        SystemMaps.initMapsMOM();
        JettyWebSocketServer.startServer(Ports.webSocketPort);
    }

    public static void main(String[] args)
    {
        init();
        while(true)
        {
            while (commands.peek() == null)
            {
                x++;
            }
            ICommand c = commands.poll();
            ProcessCmd.processCmd(c);
        }

    }
}
