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
    public static String FramesFromHPC_IN = "FramesFromHPC_IN155";
    public static String FramesFromHPC_OUT = "FramesFromHPC_OUT155";

    public static String ProcessedFramesFromHPC = "ProcessedFrames";
    public static String CompareRuns = "CompareRuns";

    public static String  configurations ="FARES";
    public static void setTopicsNames(String clusterName){
        cmdFromHpcMaster=cmdFromHpcMaster+clusterName;
        FramesFromHPC_IN=FramesFromHPC_IN+clusterName;
        FramesFromHPC_OUT=FramesFromHPC_OUT+clusterName;
        ratesFromMachines=ratesFromMachines+clusterName;
    }
}
