package SwitchAnalyzer.Network;

import org.pcap4j.core.*;
import org.pcap4j.util.LinkLayerAddress;
import org.pcap4j.util.MacAddress;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;
import java.util.Arrays;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

public class NIC_INFO {
    private static String NICAdapterName;
    private static MacAddress NICMacAddress;
    private static InetAddress NICIpAddress;
    private InetAddress NICstaticIPAddress;
    public static  MacAddress detectMyMacAddress() {
        InetAddress ipAddress = null;
        try {
            ipAddress = InetAddress.getLocalHost();
            System.out.println(ipAddress);
            NetworkInterface networkInterface = NetworkInterface.getByInetAddress(ipAddress);
            NICIpAddress=ipAddress;
            if (networkInterface != null) {
                byte[] macAddressBytes = networkInterface.getHardwareAddress();
                if (macAddressBytes != null) {
                    MacAddress macAddress = MacAddress.getByAddress(macAddressBytes);
                    String adapterName = getAdapterName(macAddress);
                    if (adapterName != null) {
                        System.out.println("Adapter name: " + adapterName);
                    }
                    NICMacAddress=macAddress;
                    return macAddress;
                }
            }
        } catch (UnknownHostException | SocketException e) {
        }
        return null;
    }

    private static String getAdapterName(MacAddress macAddress) {
        try {
            List<PcapNetworkInterface> interfaces = Pcaps.findAllDevs();
            for (PcapNetworkInterface iface : interfaces) {
                if (iface.getLinkLayerAddresses().contains(macAddress)) {
                    NICAdapterName =iface.getName();
                    return iface.getName();
                }
            }
        } catch (PcapNativeException e) {

        }
        return null;
    }
    public void setStaticIPaddress(String ipAddr) throws SocketException  {
        String interfaceName = NICAdapterName; // replace with your interface name
        String ipAddress = ipAddr; // replace with your IP address
        String subnetMask = "255.255.255.0"; // replace with your subnet mask
        String gateway = "192.168.1.1"; // replace with your default gateway
        String dns = "8.8.8.8"; // replace with your DNS server

        String command = "sudo ifconfig " + interfaceName + " " + ipAddress + " netmask " + subnetMask + " up && " +
                "sudo route add default gw " + gateway + " " + interfaceName + " && " +
                "echo \"nameserver " + dns + "\" | sudo tee /etc/resolv.conf > /dev/null";

        try {
            Process process = Runtime.getRuntime().exec(new String[]{"bash", "-c", command});

            // read the output of the command
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }

            // wait for the command to finish and get its exit status
            int exitCode = process.waitFor();
            System.out.println("Command exited with status " + exitCode);

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

}
