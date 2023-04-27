package SwitchAnalyzer.Starters;

import java.io.*;

public class ZookeeperServerStarter {
    public static void  start() throws IOException {
        String workingDirectory = "/home/ziad/kafka_2.13-3.2.3/";
        ProcessBuilder builder = new ProcessBuilder(
                workingDirectory + "bin/zookeeper-server-start.sh",
                workingDirectory + "config/zookeeper.properties");
        builder.redirectErrorStream(true);
        Process process = builder.start();

        // read the output of the process
        InputStream is = process.getInputStream();
        InputStreamReader isr = new InputStreamReader(is);
        BufferedReader br = new BufferedReader(isr);
        String line;
        while ((line = br.readLine()) != null) {
            System.out.println(line);
        }

        // wait for the process to complete
        int exitCode = 0;
        try {
            exitCode = process.waitFor();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        System.out.println("Kafka server exited with code " + exitCode);
    }
    public static void main(String[] args) throws IOException {
        ZookeeperServerStarter.start();
    }
}