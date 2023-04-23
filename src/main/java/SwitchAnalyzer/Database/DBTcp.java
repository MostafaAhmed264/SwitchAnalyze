package SwitchAnalyzer.Database;

import org.pcap4j.packet.Packet;
import org.pcap4j.packet.TcpPacket;

public class DBTcp extends DBTransportHeader
{
    private int sourcePort;
    private int destinationPort;
    private int sequenceNumber;
    private int acknowledgementNumber;
    private int dataOffset;
    private int reserved = 0;
    /**
     * Indicates that the Urgent pointer field in the TCP header is valid
     * and points to urgent data that should be processed before any other data in the receive buffer.
     */
    private boolean urgentFlag;
    /**
     * Indicates that the Acknowledgment number field in the TCP header is valid and contains the next sequence number that the receiver expects to receive
     */
    private boolean acknowledgmentFlag;
    /**
     * Indicates that the sender has pushed all data that it has to send and the receiver should process it immediately instead of buffering it
     */
    private boolean pushFlag;
    /**
     * Indicates that the connection should be reset, usually due to an error condition or when a connection is no longer valid.
     */
    private boolean resetFlag;
    /**
     * Used to initiate a connection establishment between two hosts. The SYN flag is set in the first packet of the TCP three-way handshake
     */
    private boolean synchronizeFlag;
    /**
     * Indicates that the sender has no more data to send and wants to close the connection.
     * The FIN flag is set in the final packet of the TCP three-way handshake to initiate a graceful connection termination.
     */
    private boolean finishFlag;
    /**
     * specifies the number of bytes of data that a TCP sender is willing to receive at one time from the TCP receiver.
     * This field is used for flow control and helps to prevent the sender from overwhelming the receiver with too much data
     */
    private int window;
    /**
     * TCP checksum is calculated over the entire TCP segment, including the TCP header and payload data
     */
    private String checksum;
    private int urgentPointer;
    public DBTcp(String transportHeaderName,String tcpHeaderBytes)
    {
        super(transportHeaderName,tcpHeaderBytes);
    }

    public int getSourcePort() {
        return sourcePort;
    }

    public void setSourcePort(int sourcePort) {
        this.sourcePort = sourcePort;
    }

    public int getDestinationPort() {
        return destinationPort;
    }

    public void setDestinationPort(int destinationPort) {
        this.destinationPort = destinationPort;
    }

    public int getSequenceNumber() {
        return sequenceNumber;
    }

    public void setSequenceNumber(int sequenceNumber) {
        this.sequenceNumber = sequenceNumber;
    }

    public int getAcknowledgementNumber() {
        return acknowledgementNumber;
    }

    public void setAcknowledgementNumber(int acknowledgementNumber) {
        this.acknowledgementNumber = acknowledgementNumber;
    }

    public int getDataOffset() {
        return dataOffset;
    }

    public void setDataOffset(int dataOffset) {
        this.dataOffset = dataOffset;
    }

    public int getReserved() {
        return reserved;
    }

    public void setReserved(int reserved) {
        this.reserved = reserved;
    }

    public boolean isUrgentFlag() {
        return urgentFlag;
    }

    public void setUrgentFlag(boolean urgentFlag) {
        this.urgentFlag = urgentFlag;
    }

    public boolean isAcknowledgmentFlag() {
        return acknowledgmentFlag;
    }

    public void setAcknowledgmentFlag(boolean acknowledgmentFlag) {
        this.acknowledgmentFlag = acknowledgmentFlag;
    }

    public boolean isPushFlag() {
        return pushFlag;
    }

    public void setPushFlag(boolean pushFlag) {
        this.pushFlag = pushFlag;
    }

    public boolean isResetFlag() {
        return resetFlag;
    }

    public void setResetFlag(boolean resetFlag) {
        this.resetFlag = resetFlag;
    }

    public boolean isSynchronizeFlag() {
        return synchronizeFlag;
    }

    public void setSynchronizeFlag(boolean synchronizeFlag) {
        this.synchronizeFlag = synchronizeFlag;
    }

    public boolean isFinishFlag() {
        return finishFlag;
    }

    public void setFinishFlag(boolean finishFlag) {
        this.finishFlag = finishFlag;
    }

    public int getWindow() {
        return window;
    }

    public void setWindow(int window) {
        this.window = window;
    }

    public String getChecksum() {
        return checksum;
    }

    public void setChecksum(String checksum) {
        this.checksum = checksum;
    }

    public int getUrgentPointer() {
        return urgentPointer;
    }

    public void setUrgentPointer(int urgentPointer) {
        this.urgentPointer = urgentPointer;
    }
    /**
     * Input : Packet received
     * Output : void
     * Description :
     * This function takes the packet and convert it to Tcp packet
     * and then sets every part of the header of the tcp in the DBTcp
     * and also it sets the sending and receiving ports of the DBFrame object
     */
    public void getTransportHeaderDetails(Packet packet,DBFrame frame)
    {
        TcpPacket tcpPacket = packet.get(TcpPacket.class);
        TcpPacket.TcpHeader tcpHeader = tcpPacket.getHeader();
       // System.out.println(tcpHeader.toString());
        //The payload of the tcp which is the data
        //String payloadOfTcp = tcpPacket.getPayload().toString();
        //System.out.println(payloadOfTcp);
        setSourcePort(tcpHeader.getSrcPort().valueAsInt());
        frame.setSendingPort(getSourcePort());
        setDestinationPort(tcpHeader.getDstPort().valueAsInt());
        frame.setRecievingPort(getDestinationPort());
        setSequenceNumber(tcpHeader.getSequenceNumber());
        setAcknowledgementNumber(tcpHeader.getAcknowledgmentNumber());
        setDataOffset(tcpHeader.getDataOffsetAsInt());
        setUrgentFlag(tcpHeader.getUrg());
        setAcknowledgmentFlag(tcpHeader.getAck());
        setPushFlag(tcpHeader.getPsh());
        setResetFlag(tcpHeader.getRst());
        setSynchronizeFlag(tcpHeader.getSyn());
        setFinishFlag(tcpHeader.getFin());
        setWindow(tcpHeader.getWindowAsInt());
        setChecksum(Integer.toHexString(tcpHeader.getChecksum() & 0xffff));
        setUrgentPointer(tcpHeader.getUrgentPointerAsInt());
    }
}
