package SwitchAnalyzer.Database;

public class Switch_DB extends Switch
{
    public Long totalnoofports;
    public Switch_DB(String switchName , Long totalnoofports) {
        super(switchName);
        this.totalnoofports = totalnoofports;
    }
}
