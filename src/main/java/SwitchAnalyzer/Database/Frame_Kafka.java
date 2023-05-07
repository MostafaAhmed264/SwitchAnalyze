package SwitchAnalyzer.Database;

public class Frame_Kafka implements Frame
{
    public String frame_json;
    public String bytes;
    public String runNo;
    public String switchName;
    public Frame_Kafka(String frame_json,String runNo, String switchName) {
        this.frame_json = frame_json;
        this.switchName = switchName;
    }
}
