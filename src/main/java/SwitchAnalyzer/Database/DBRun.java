package SwitchAnalyzer.Database;

public class DBRun {

    private long runno;
    public String starttimestamp;
    public String endtimestamp;
    private float packetloss;
    private float latency;
    private float throughput;
    private float successfulframespercentage;
    private float frameswitherrorspercentage;
    public DBRun(){

    }
    public DBRun(String startTimeStamp, String endTimeStamp, float packetLoss, float latency, float throughput, float successfulFramesPercentage, float framesWithErrorsPercentage) {
        this.runno = DBConnect.getLastRun();
        this.starttimestamp = startTimeStamp;
       this.endtimestamp = endTimeStamp;
        this.packetloss = packetLoss;
        this.latency = latency;
        this.throughput = throughput;
        this.successfulframespercentage = successfulFramesPercentage;
        this.frameswitherrorspercentage = framesWithErrorsPercentage;
    }

    public void setRunNo(long runNo) {
        this.runno = runNo;
    }

    public void setStartTimeStamp(String startTimeStamp) {
        this.starttimestamp = startTimeStamp;
    }

    public void setEndTimeStamp(String endTimeStamp) {
        this.endtimestamp = endTimeStamp;
    }

    public void setPacketLoss(float packetLoss) {
        this.packetloss = packetLoss;
    }

    public void setLatency(float latency) {
        this.latency = latency;
    }

    public void setThroughput(float throughput) {
        this.throughput = throughput;
    }

    public void setSuccessfulFramesPercentage(float successfulFramesPercentage) {
        this.successfulframespercentage = successfulFramesPercentage;
    }

    public void setFramesWithErrorsPercentage(float framesWithErrorsPercentage) {
        this.frameswitherrorspercentage = framesWithErrorsPercentage;
    }

    public long getRunNo() {
        return runno;
    }

    public String getStartTimeStamp() {
        return starttimestamp;
    }

    public String getEndTimeStamp() {
        return endtimestamp;
    }

    public float getPacketLoss() {
        return packetloss;
    }

    public float getLatency() {
        return latency;
    }

    public float getThroughput() {
        return throughput;
    }

    public float getSuccessfulFramesPercentage() {
        return successfulframespercentage;
    }

    public float getFramesWithErrorsPercentage() {
        return frameswitherrorspercentage;
    }
}
