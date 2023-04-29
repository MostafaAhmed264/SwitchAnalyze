package SwitchAnalyzer.Network.ErrorDetection;

import org.pcap4j.packet.EthernetPacket;
import org.pcap4j.packet.IllegalRawDataException;
import org.pcap4j.packet.Packet;

import java.util.Arrays;
import java.util.zip.CRC32;

public class CRC extends ErrorDetectingAlgorithms {
    public CRC(boolean injectError) { this.injectError = injectError ; }
    public CRC(){}


    @Override
    public Packet.Builder buildHeader(Packet.Builder prevBuilder)
    {
        byte[] packetBytes = prevBuilder.build().getRawData();
        byte[] crcBytes =calculateCRC(packetBytes);
        byte[] packetBytesWithCrc=appendCRCbytes(packetBytes, crcBytes);

        EthernetPacket packetWithCrc;
        try { packetWithCrc = EthernetPacket.newPacket(packetBytesWithCrc, 0, packetBytesWithCrc.length); }
        catch (IllegalRawDataException e) { throw new RuntimeException(e); }
        return packetWithCrc.getBuilder();
    }
    public byte[] calculateCRC(byte[] packetBytes)
    {
        CRC32 crcOBJ = new CRC32();
        crcOBJ.update(packetBytes);
        int crc = (int) crcOBJ.getValue();
        byte[] crcBytes = new byte[4];
        crcBytes[0] = (byte) ((crc >> 24) & 0xFF);
        crcBytes[1] = (byte) ((crc >> 16) & 0xFF);
        crcBytes[2] = (byte) ((crc >> 8) & 0xFF);
        crcBytes[3] = (byte) (crc & 0xFF);
        if (!injectError)
        {
            return crcBytes;
        }
         crcBytes[3] ^=1;
        return  crcBytes;
    }

    public byte[] appendCRCbytes(byte[] packetBytes,byte[] crcBytes)
    {
        byte[] packetBytesWithCrc = new byte[packetBytes.length + crcBytes.length];
        System.arraycopy(packetBytes, 0, packetBytesWithCrc, 0, packetBytes.length);
        System.arraycopy(crcBytes, 0, packetBytesWithCrc, packetBytes.length, crcBytes.length);
        return packetBytesWithCrc;
    }

    @Override
    public boolean isAlgorithmCorrect(byte[] packet)
    {
        int crcOffset = packet.length - 4;
        byte[] packetWithoutCRC = new byte[crcOffset];
        byte[] recievedCRCbytes =new byte[4];
        System.arraycopy(packet, 0, packetWithoutCRC, 0, crcOffset);
        System.arraycopy(packet, crcOffset, recievedCRCbytes, 0, 4);

        byte[] calculatedCRCbytes = calculateCRC(packetWithoutCRC);

        return Arrays.equals(recievedCRCbytes, calculatedCRCbytes);
    }
}
