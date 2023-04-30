package SwitchAnalyzer.Sockets;

import SwitchAnalyzer.Commands.ICommand;
import SwitchAnalyzer.MainHandler_MOM;
import SwitchAnalyzer.miscellaneous.JSONConverter;
import SwitchAnalyzer.miscellaneous.SystemMaps;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.*;
import org.eclipse.jetty.websocket.server.WebSocketHandler;
import org.eclipse.jetty.websocket.servlet.WebSocketServlet;
import org.eclipse.jetty.websocket.servlet.WebSocketServletFactory;

import java.io.IOException;
import java.util.Queue;

@WebSocket
public class JettyWebSocketServer
{
    static Session session;
    private static volatile int x = 0;

    @OnWebSocketConnect
    public void onConnect(Session session)
    {
        this.session = session;
        System.out.println("Connected: " + session.getRemoteAddress().getHostName());
    }

    @OnWebSocketClose
    public void onClose(Session session, int statusCode, String reason)
    {
        System.out.println("Closed: " + session.getRemoteAddress().getHostName() + " " + statusCode + " " + reason);
    }

    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws IOException
    {
        byte[] command = message.getBytes();
        try { parseCommand(command, MainHandler_MOM.commands); }
        catch (JsonProcessingException e) { throw new RuntimeException(e);}
    }
    public static void parseCommand(byte[] command, Queue<ICommand> cmdQueue) throws JsonProcessingException
    {
        String jsonStr = new String(command);
        int commandTypeIndex = Character.getNumericValue(jsonStr.charAt(0));
        System.out.println(commandTypeIndex);
        jsonStr = jsonStr.replaceFirst("[0-9]*","");
        System.out.println("UserRequestHandler: "+ jsonStr);
        ICommand c= JSONConverter.fromJSON(jsonStr, SystemMaps.commandClasses.get(commandTypeIndex));
        cmdQueue.add(c);
    }

    public static void writeMessage (String message)
    {
        try { session.getRemote().sendString(message);}
        catch (Exception e) {}
    }
    public static class MyWebSocketServlet extends WebSocketServlet
    {
        @Override
        public void configure(WebSocketServletFactory factory) {
            factory.register(JettyWebSocketServer.class);
        }
    }


    public static void startServer (int port)
    {
        Server server = new Server(port);
        WebSocketHandler wsHandler = new WebSocketHandler()
        {
            @Override
            public void configure(WebSocketServletFactory factory)
            {
                factory.register(JettyWebSocketServer.class);
                factory.getPolicy().setIdleTimeout(0);
            }
        };
        server.setHandler(wsHandler);
        try
        {
            server.start();
            System.out.println("WebSocket server started on port: " + port);
            //server.join();
        }
        catch (Exception e) {throw new RuntimeException(e);}
    }
}