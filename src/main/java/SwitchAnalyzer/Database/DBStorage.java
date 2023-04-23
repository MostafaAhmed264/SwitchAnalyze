package SwitchAnalyzer.Database;


import java.io.BufferedReader;
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
}
