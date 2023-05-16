package SwitchAnalyzer.Kafka;

/**
 * this class will hold all the topics used inside our system
 */
public class Topics {
    public static  String configurationsTopic="CONFIG";
    public static String  cmdFromMOM="CMDFromMOMM";
    public static String  ratesFromHPCs="RatesFromHPCs1";
    public static String  ratesFromMachines = "RatesFromMachines155";
    public static String  cmdFromHpcMaster = "CMD_FromHPC_Master155";
    public static String FramesFromHPC = "FramesFromHPC155";
    public static String ProcessedFramesFromHPC = "ProcessedFrames";
    public static String CompareRuns = "CompareRuns";

    public static String  configurations ="FARES";
    public static void setTopicsNames(String clusterName){
        cmdFromHpcMaster=cmdFromHpcMaster+clusterName;
    ratesFromHPCs=ratesFromHPCs+clusterName;
    ratesFromMachines=ratesFromMachines+clusterName;
    }
}
