package SwitchAnalyzer.Database;

import java.sql.Timestamp;
import java.util.Date;

public class DBRun {

    private long runNo;
    private Date startTimeStamp;
    private Date endTimeStamp;
    private float packetLoss;
    private float latency;
    private float throughput;
    private float successfulFramesPercentage;
    private float framesWithErrorsPercentage;

    public DBRun(Date startTimeStamp, Date endTimeStamp, float packetLoss, float latency, float throughput, float successfulFramesPercentage, float framesWithErrorsPercentage) {
        this.runNo = DBConnect.getLastRun();
        this.startTimeStamp = startTimeStamp;
        this.endTimeStamp = endTimeStamp;
        this.packetLoss = packetLoss;
        this.latency = latency;
        this.throughput = throughput;
        this.successfulFramesPercentage = successfulFramesPercentage;
        this.framesWithErrorsPercentage = framesWithErrorsPercentage;
    }

    public void setRunNo(long runNo) {
        this.runNo = runNo;
    }

    public void setStartTimeStamp(Date startTimeStamp) {
        this.startTimeStamp = startTimeStamp;
    }

    public void setEndTimeStamp(Date endTimeStamp) {
        this.endTimeStamp = endTimeStamp;
    }

    public void setPacketLoss(float packetLoss) {
        this.packetLoss = packetLoss;
    }

    public void setLatency(float latency) {
        this.latency = latency;
    }

    public void setThroughput(float throughput) {
        this.throughput = throughput;
    }

    public void setSuccessfulFramesPercentage(float successfulFramesPercentage) {
        this.successfulFramesPercentage = successfulFramesPercentage;
    }

    public void setFramesWithErrorsPercentage(float framesWithErrorsPercentage) {
        this.framesWithErrorsPercentage = framesWithErrorsPercentage;
    }

    public long getRunNo() {
        return runNo;
    }

    public Date getStartTimeStamp() {
        return startTimeStamp;
    }

    public Date getEndTimeStamp() {
        return endTimeStamp;
    }

    public float getPacketLoss() {
        return packetLoss;
    }

    public float getLatency() {
        return latency;
    }

    public float getThroughput() {
        return throughput;
    }

    public float getSuccessfulFramesPercentage() {
        return successfulFramesPercentage;
    }

    public float getFramesWithErrorsPercentage() {
        return framesWithErrorsPercentage;
    }
}
