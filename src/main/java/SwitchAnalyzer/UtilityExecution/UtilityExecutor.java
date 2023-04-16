package SwitchAnalyzer.UtilityExecution;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class UtilityExecutor
{
    public static Map<String, String> result =  new ConcurrentHashMap<>();
    public static ArrayList<IExecutor> executors = new ArrayList<>();

    public static void executeUtils()
    {
        List<Thread> threads = new ArrayList<>();
        for (int i = 0; i < executors.size(); i++)
        {
            final int index = i;
            Thread thread = new Thread( () -> executors.get(index).execute());
            threads.add(thread);
            thread.start();
        }
        for (Thread thread : threads)
        {
            try { thread.join(); }
            catch (InterruptedException e) {System.out.printf("in MasterConsumer: %s%n", e.getMessage());}
        }
    }
    public static void clearUtils(){ executors.clear(); }
}
