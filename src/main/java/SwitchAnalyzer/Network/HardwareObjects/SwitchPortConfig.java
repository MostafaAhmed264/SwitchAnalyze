package SwitchAnalyzer.Network.HardwareObjects;


import SwitchAnalyzer.Network.PacketInfo;
import SwitchAnalyzer.Network.Utilities;

import java.util.ArrayList;

public class SwitchPortConfig {
   public ArrayList<PacketInfo> packetInfos;
    public ArrayList<Utilities> utilities;
   public int rate;

   public String mode; // sender or receiver

}

