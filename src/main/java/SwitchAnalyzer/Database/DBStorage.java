package SwitchAnalyzer.Database;


import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.FileStore;
import java.nio.file.Files;
import java.nio.file.Paths;

public class DBStorage {
    // this variable will have the value of the remaining disk space in a cassandra node
    private static String remainingDiskSpace;
    // this variable will have the value of the acquired disk space by a cassandra node
    private static String acquiredDiskSpace ;

    public static String getAcquiredDiskSpace() {
        return acquiredDiskSpace;
    }

    public static void setAcquiredDiskSpace(String acquiredDiskSpace) {
        DBStorage.acquiredDiskSpace = acquiredDiskSpace;
    }

    public static String getRemainingDiskSpace() {
        return remainingDiskSpace;
    }

    public static void setRemainingDiskSpace(String remainingDiskSpace) {
        DBStorage.remainingDiskSpace = remainingDiskSpace;
    }
    public static void acquiredStorage()
    {
        try {
            String command = "nodetool info | grep 'Load'";
            Process process = Runtime.getRuntime().exec(new String[] { "/bin/sh", "-c", command });
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            acquiredDiskSpace=reader.readLine().split(":")[1].trim();
        }
        catch (Exception e)
        {
            System.out.println("Error in getting the load of the node");
        }
    }
    public static void remainingStorage()
    {
        remainingStorage_default();
    }
    private static String formatSize(long size) {
        final long KB = 1024;
        final long MB = KB * 1024;
        final long GB = MB * 1024;
        if (size >= GB) {
            return String.format("%.2f GB", (double) size / GB);
        } else if (size >= MB) {
            return String.format("%.2f MB", (double) size / MB);
        } else if (size >= KB) {
            return String.format("%.2f KB", (double) size / KB);
        } else {
            return String.format("%d bytes", size);
        }
    }
    private static void remainingStorage_default()
    {
        try {
        // Get the file store for the current directory
        FileStore store = Files.getFileStore(Paths.get("."));

        // Get the available space in bytes
        long availableSpace = store.getUsableSpace();

        // Convert bytes to a human-readable format
        String[] units = {"B", "KB", "MB", "GB", "TB"};
        int unitIndex = 0;
        double remainingSpace = availableSpace;
        while (remainingSpace > 1024 && unitIndex < units.length - 1) {
            remainingSpace /= 1024;
            unitIndex++;
        }
        remainingDiskSpace = String.format("%.2f %s", remainingSpace, units[unitIndex]);
        }
        catch (Exception e)
        {
            System.out.println("Error in getting the remaining storage of the node");
        }
    }
    private static void remainingStorage_dynamicLocation()
    {
        String dataLocation = dataLocation();
        File file = new File(dataLocation);
        long freeSpace = file.getFreeSpace();
        long usableSpace = file.getUsableSpace();
        String freeSpaceStr = formatSize(freeSpace);
        remainingDiskSpace = freeSpaceStr;
    }
    private static String dataLocation()
    {
        String command = "nodetool info";
        String output = executeCommand(command);

        // Parse the output to get the data directory path
        String dataDirectory = "";
        String[] lines = output.split("\\r?\\n");
        for (String line : lines) {
            if (line.startsWith("Data Center")) {
                int startIndex = line.indexOf("rack=");
                if (startIndex != -1) {
                    int endIndex = line.indexOf(",", startIndex);
                    if (endIndex != -1) {
                        dataDirectory = line.substring(startIndex + 5, endIndex);
                        break;
                    }
                }
            }
        }
        return dataDirectory;
    }
    private static String executeCommand(String command) {
        StringBuilder output = new StringBuilder();
        Process process;
        try {
            process = Runtime.getRuntime().exec(command);
            process.waitFor();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line).append("\n");
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return output.toString();
    }
}
