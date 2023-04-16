package SwitchAnalyzer.UtilityExecution;

import SwitchAnalyzer.NamingConventions;
import SwitchAnalyzer.Network.Observer;


public class RateExecutor implements IExecutor
{
    public void execute() {
        float rate = Observer.getRate();
        UtilityExecutor.result.put(NamingConventions.rates, Float.toString(rate));
        System.out.println("Rate: "+rate);
    }
}
