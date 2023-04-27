package SwitchAnalyzer.Network;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class ExecutorCMD {
    public String outputStore;

    public void runCommand(String command) {
        try {
            // Execute the command using the Runtime class
            Runtime runtime = Runtime.getRuntime();
            Process process = runtime.exec(command);

            // Get the output of the command as a string
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            StringBuilder output = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line).append("\n");
            }

            // Store the output in the outputStore member variable
            outputStore = output.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}