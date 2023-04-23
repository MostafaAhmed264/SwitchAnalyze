package SwitchAnalyzer.Database;

public class DBEthernetHeader
{
    private String srcAddress;
    private String dstAddress;
    private String type;

    public DBEthernetHeader(){}
    public DBEthernetHeader(String srcAddress, String dstAddress, String type) {
        srcAddress = srcAddress;
        dstAddress = dstAddress;
        this.type = type;
    }

    public String getSrcAddress() {
        return srcAddress;
    }

    public void setSrcAddress(String srcAddress) {
        srcAddress = srcAddress;
    }

    public String getDstAddress() {
        return dstAddress;
    }

    public void setDstAddress(String dstAddress) {
        dstAddress = dstAddress;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
