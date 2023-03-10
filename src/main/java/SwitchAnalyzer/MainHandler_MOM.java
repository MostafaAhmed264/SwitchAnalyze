package SwitchAnalyzer;

import SwitchAnalyzer.Collectors.*;
import SwitchAnalyzer.Commands.*;
import SwitchAnalyzer.Commands.ICommand;
import SwitchAnalyzer.Machines.MOM;
import SwitchAnalyzer.Machines.MachineNode;
import SwitchAnalyzer.Network.Ports;
import SwitchAnalyzer.Sockets.UserRequestHandler;
import SwitchAnalyzer.Sockets.WebSocketServer;
import SwitchAnalyzer.miscellaneous.GlobalVariable;
import SwitchAnalyzer.Machines.MasterOfHPC;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public class MainHandler_MOM {
    public static WebSocketServer server;
    static Queue<ICommand> commands = new LinkedList<>();
    static ArrayList<Class<? extends ICommandMOM>> commandClasses = new ArrayList<>();
    static ArrayList<Collector> collectors = new ArrayList<>();
    static volatile int x;
    public static MOM masterOfMasters;
    //TODO: should have an object of MOM in order to be used by the collectors?
    public static void init()
    {
        /*
            read the config text file and initialize the Global variables.
         */
        /*
        TODO: should initialize MOM and add all HPCs to it?
         */
        /*
            run the Mapping algorithm between ports and HPCs
         */
        server = new WebSocketServer(Ports.webSocketPort);
        GlobalVariable.portHpcMap.put(1, new MasterOfHPC(0, 2));
        GlobalVariable.portHpcMap.get(1).childNodes.add(new MachineNode(0));
        GlobalVariable.portHpcMap.get(1).childNodes.add(new MachineNode(1));
        MOM masterOfMasters = new MOM();
        masterOfMasters.HPCs.add(GlobalVariable.portHpcMap.get(0));
        commandClasses.add(StartRunCommand_MOM.class);
        commandClasses.add(RetrieveCmd_MOM.class);
        collectors.add(new RatesCollectorMOM());
        collectors.add(new PLossCollectorMOM());
    }

    public static void main(String[] args)
    {
        init();
        Thread t1 = new Thread(() -> UserRequestHandler.readCommands(server, Ports.webSocketPort,
                8888, commands));
        t1.start();
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
