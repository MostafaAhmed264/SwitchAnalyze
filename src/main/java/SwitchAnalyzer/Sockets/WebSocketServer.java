package SwitchAnalyzer.Sockets;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.ArrayList;


public class WebSocketServer {
    private final ServerSocket server;
    private Socket client;
    private InputStream in;
    private OutputStream out;
    private final ArrayList<Byte> conctPayLoadReceived = new ArrayList<Byte>();
    private final ArrayList<Byte> conctPayLoadWritten = new ArrayList<Byte>();

    public WebSocketServer(int portNumber) {
        try {
            server = new ServerSocket(portNumber);
            System.out.println("Server has started on 127.0.0.1:" + portNumber);
            HandShake();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void HandShake() {
        System.out.println("Waiting for a connectionâ€¦");
        try {
            client = server.accept();
            System.out.println("A client connected.");

            //InputOutput Streams
            in = client.getInputStream();
            out = client.getOutputStream();
            Scanner s = new Scanner(in, StandardCharsets.UTF_8);

            //HandShaking

            String data = s.useDelimiter("\\r\\n\\r\\n").next();
            Matcher get = Pattern.compile("^GET").matcher(data);

            if (get.find()) {

//                System.out.println(data);
//                System.out.println();
//                System.out.println();
//                Matcher pattern = Pattern.compile("sec-websocket-key: (.*)").matcher(data);
//                pattern.find();
//                System.out.println(pattern.group(1));
//                System.out.println();
//                System.out.println();

                //It's Sec-WebSocket-Key in other formats
                Matcher match = Pattern.compile("sec-websocket-key: (.*)").matcher(data);
                match.find();
                byte[] response = ("HTTP/1.1 101 Switching Protocols\r\n"
                        + "Connection: Upgrade\r\n"
                        + "Upgrade: websocket\r\n"
                        + "Sec-WebSocket-Accept: "
                        + Base64.getEncoder().encodeToString(MessageDigest.getInstance("SHA-1").digest((match.group(1) + "258EAFA5-E914-47DA-95CA-C5AB0DC85B11").getBytes(StandardCharsets.UTF_8)))
                        + "\r\n\r\n").getBytes(StandardCharsets.UTF_8);
                out.write(response, 0, response.length);
            }

            System.out.println("HandShaking is done, Your WebSocket is ready to use!");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public byte[] readFromSocket(int maxMessageLength) {
        byte[] messageReceived = new byte[maxMessageLength];
        try {
            in.read(messageReceived, 0, maxMessageLength);


            //Checking if client is disconnecting
            if (checkDisconnection(messageReceived)) {
                System.out.println("Client Disconnected ...");
                return null;
            }

            //Analyzing the payLoad length
            int tempPayLoadLength = Byte.toUnsignedInt(messageReceived[1]) - 128;


            //Decode
            byte[] payLoadDecoded = decode(messageReceived, tempPayLoadLength);

            return (payLoadDecoded);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }


    private boolean checkDisconnection(byte[] messageReceived) {
        int disconnectingValue = 136;
        return Byte.toUnsignedInt(messageReceived[0]) == disconnectingValue;
    }


    private byte[] decode(byte[] messageReceived, int tempPayLoadLength) {
        int payLoadLength;
        byte[] payLoadDecoded = null;

        //First Mode Decoding (PayLoad has a max length of 125 Bytes)
        if (tempPayLoadLength > 0 && tempPayLoadLength <= 125) {
            System.out.println("******Iam in Mode1******");
            payLoadLength = tempPayLoadLength;
            payLoadDecoded = new byte[payLoadLength];
            byte[] key = new byte[]{messageReceived[2], messageReceived[3], messageReceived[4], messageReceived[5]};

            //System.out.print("Message Received: ");
            for (int i = 0; i < payLoadLength; i++) {
                payLoadDecoded[i] = (byte) (messageReceived[i + 6] ^ key[i & 0x3]);
                conctPayLoadReceived.add(payLoadDecoded[i]);
                // System.out.print((char) (Byte.toUnsignedInt(payLoadDecoded[i])));
            }
        }

        //Second Mode (PayLoad has a max length of 2^16 Bytes)
        else if (tempPayLoadLength == 126) {
            System.out.println("******Iam in Mode2******");
            payLoadLength = ((Byte.toUnsignedInt(messageReceived[2])) << 8) | Byte.toUnsignedInt(messageReceived[3]);
            payLoadDecoded = new byte[payLoadLength];
            byte[] key = new byte[]{messageReceived[4], messageReceived[5], messageReceived[6], messageReceived[7]};

            //System.out.print("Message Received: ");
            for (int i = 0; i < payLoadLength; i++) {
                payLoadDecoded[i] = (byte) (messageReceived[i + 8] ^ key[i & 0x3]);
                conctPayLoadReceived.add(payLoadDecoded[i]);
                // System.out.print((char) (Byte.toUnsignedInt(payLoadDecoded[i])));
            }
        }

        //Third Mode (PayLoad has a max length of 2^64 Bytes), but we will only be able to use a max length of 2^32
        //because the static array that is given to the read function must be initialized
        // with a size in int which is 4 bytes only.
        // It's not robust
//        else if (tempPayLoadLength == 127) {
//            System.out.println("******Iam in Mode3******");
//            payLoadLength =(int)(((Byte.toUnsignedLong(messageReceived[2]))<< 56) | (Byte.toUnsignedLong(messageReceived[3])<<48)
//                             | (Byte.toUnsignedLong(messageReceived[4])<<40 ) | (Byte.toUnsignedLong(messageReceived[5])<<32)
//                            | (Byte.toUnsignedLong(messageReceived[6])<<24) | (Byte.toUnsignedLong(messageReceived[7])<<16)
//                            | (Byte.toUnsignedLong(messageReceived[8])<<8)| (Byte.toUnsignedLong(messageReceived[9])));
//            payLoadDecoded = new byte[payLoadLength];
//            byte[] key = new byte[]{messageReceived[10], messageReceived[11], messageReceived[12], messageReceived[13]};
//
//            //System.out.print("Message Received: ");
//            for (int i = 0; i < payLoadLength; i++) {
//                payLoadDecoded[i] = (byte) (messageReceived[i + 10] ^ key[i & 0x3]);
//                conctMessageReceived.add(payLoadDecoded[i]);
//                // System.out.print((char) (Byte.toUnsignedInt(payLoadDecoded[i])));
//            }
//        }

        return payLoadDecoded;
    }


    public ArrayList<Byte> getConctPayLoadReceived() {
        return conctPayLoadReceived;
    }

    public ArrayList<Byte> getConctPayLoadWritten() {
        return conctPayLoadWritten;
    }

    //This Function sends an unmasked frame through the socket , with FIN = 1 , opcode = 1;
    public void writeToSocket(byte[] payLoadToClient) {
        byte[] messageToClient = encode(payLoadToClient);

        try {
            out.write(messageToClient);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private byte[] encode(byte[] payLoadToClient) {
        byte[] messageToClient = null;

        //First Mode Encoding (PayLoad has a max length of 125 Bytes)
        if (payLoadToClient.length > 0 && payLoadToClient.length <= 125) {
            messageToClient = new byte[payLoadToClient.length + 2];
            messageToClient[0] = (byte) 129; //FIN = 1 , opcode = 1;
            messageToClient[1] = (byte) payLoadToClient.length; //Setting up the length of the payload

            //Copying the payload to the message frame that will be written
            for (int i = 0; i < payLoadToClient.length; i++) {
                messageToClient[i + 2] = payLoadToClient[i];
                conctPayLoadWritten.add(payLoadToClient[i]);
            }
        }

        //Second Mode Encoding (PayLoad has a max length of 2^16 Bytes)
        else if (payLoadToClient.length >= 126 && payLoadToClient.length <= 65536) {
            messageToClient = new byte[payLoadToClient.length + 4];
            messageToClient[0] = (byte) 129; //FIN = 1 , opcode = 1;

            //Setting up the length of the payload
            messageToClient[1] = (byte) 126;
            messageToClient[2] = (byte) ((payLoadToClient.length & (0x0000FF00)) >> 8);
            messageToClient[3] = (byte) (payLoadToClient.length & (0x000000FF));

            //Copying the payload to the message frame that will be written
            for (int i = 0; i < payLoadToClient.length; i++) {
                messageToClient[i + 4] = payLoadToClient[i];
                conctPayLoadWritten.add(payLoadToClient[i]);
            }

        }


        return messageToClient;
    }

    public static void main(String[] args) throws JsonProcessingException {

        WebSocketServer x = new WebSocketServer(8080);

        //Test Mode2 with (read/write)
        byte[] testMode2 = "Mohamed Ayman".getBytes();

        //boolean matched = true;

        while (true) {
            x.writeToSocket(testMode2);
            byte[] readMessage = x.readFromSocket(100000);

            //if readMessage returned NULL, it means that the client disconnected
            if (readMessage != null) {
                System.out.print("Message Received: ");

                for (int i = 0; i < readMessage.length; i++) {
                    System.out.print((char) (Byte.toUnsignedInt(readMessage[i])));
//                    if (Byte.toUnsignedInt(readMessage[i]) != Byte.toUnsignedInt(testMode2[i]))
//                        matched = false;
                }

                System.out.println();
                System.out.println("Number of Bytes Received: " + readMessage.length);

//                if (matched){
//                    System.out.println("Received Correctly!");
//                }
            } else {
                ArrayList<Byte> conctPayLoadReceived = x.getConctPayLoadReceived();
                System.out.print("All Messages Received Throughout the Connection with This Client: ");

                for (int i = 0; i < conctPayLoadReceived.size(); i++) {
                    System.out.print((char) (Byte.toUnsignedInt(conctPayLoadReceived.get(i))));
                }

                System.out.println();
                System.out.println("Total Bytes Received: " + conctPayLoadReceived.size());

                ArrayList<Byte> conctPayLoadWritten = x.getConctPayLoadWritten();
                System.out.print("All Messages Written Throughout the Connection with This Client: ");

                for (int i = 0; i < conctPayLoadWritten.size(); i++) {
                    System.out.print((char) (Byte.toUnsignedInt(conctPayLoadWritten.get(i))));
                }

                System.out.println();
                System.out.println("Total Bytes Written: " + conctPayLoadWritten.size());

                x.HandShake();
            }
        }


    }
}
