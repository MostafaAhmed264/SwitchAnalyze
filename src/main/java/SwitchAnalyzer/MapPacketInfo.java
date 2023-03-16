package SwitchAnalyzer;

import SwitchAnalyzer.Network.*;
import SwitchAnalyzer.Network.ErrorDetection.ErrorDetectingAlgorithms;
import SwitchAnalyzer.Sockets.PacketInfoGui;

import static SwitchAnalyzer.miscellaneous.GlobalVariable.packetInfoMap;

public class MapPacketInfo implements mapObjects
{
    @Override
    public  Object map(Object object)
    {
        PacketInfoGui packetGuiInfo = (PacketInfoGui) object;
        PacketInfo result = new PacketInfo
                (
                        new PayloadBuilder(packetGuiInfo.payloadBuilder),
                        (TransportHeader) packetInfoMap.get(packetGuiInfo.transportHeader),
                        (NetworkHeader) packetInfoMap.get(packetGuiInfo.networkHeader),
                        (DataLinkHeader) packetInfoMap.get(packetGuiInfo.dataLinkHeader),
                        (ErrorDetectingAlgorithms) packetInfoMap.get(packetGuiInfo.errorDetectingAlgorithm)
                );
        result.numberOfPackets = packetGuiInfo.numberOfPackets;
        result.packetSize = packetGuiInfo.packetSize;
        result.insertErrors = packetGuiInfo.injectErrors;
        return result;
    }
}
