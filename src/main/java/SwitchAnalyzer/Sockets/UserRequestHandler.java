package SwitchAnalyzer.Sockets;


import SwitchAnalyzer.Commands.ICommand;
import SwitchAnalyzer.Commands.StartRunCommand_MOM;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.Queue;
public class UserRequestHandler
{

    static volatile int x = 0;
    static ArrayList<Class<? extends ICommand>> classes = new ArrayList<>();
    public static void readCommands(WebSocketServer webSocketServer,int portNumber, int maxMessageLength, Queue<ICommand> cmdQueue)
    {
        while (true)
        {
            byte[] command = webSocketServer.readFromSocket(maxMessageLength);
            if (command == null)
            {
                webSocketServer.HandShake();
                continue;
            }
            try { parseCommand(command, cmdQueue); }
            catch (JsonProcessingException e) { throw new RuntimeException(e);}
        }
    }
    public static void parseCommand(byte[] command, Queue<ICommand> cmdQueue) throws JsonProcessingException
    {
        String jsonStr = new String(command);
        int commandTypeIndex = Character.getNumericValue(jsonStr.charAt(0));
        jsonStr = jsonStr.replaceFirst("[0-9]*","");
        System.out.println("UserRequestHandler: "+ jsonStr);
        ObjectMapper mapper = new ObjectMapper();
        ICommand c = mapper.readValue(jsonStr, classes.get(commandTypeIndex));
        cmdQueue.add(c);
    }

    public static void writeToUser (WebSocketServer server , byte[] writtenData) {
        server.writeToSocket(writtenData);
    }

}
