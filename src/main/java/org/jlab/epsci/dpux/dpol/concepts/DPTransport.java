package org.jlab.epsci.dpux.dpol.concepts;

public class DPTransport {
    private String transportName;
    private String transClass;
    private String etName;
    private boolean etCreate;
    private int etTcpPort;
    private int etUdpPort;
    private int etWait;
    private String mAddress;
    private int etEventNum;
    private int etEventSize;
    private int inputEtChunkSize;
    private int etChunkSize;
    private String single;
    private int etGroups;
    private int etRecvBuf;
    private int etSendBuf;
    private String etMethodCon;
    private String etHostName;
    private String etSubNet;
    private String destinationEtCreate;
    private int emuDirectPort;
    private int emuMaxBuffer;
    private int emuWait;
    private String emuSubNet;
    private String fpgaLinkIp;
    private boolean emuFatPipe;
    private int tcpStreamDirectPort;
    private int tcpStreamMaxBuffer;
    private int tcpStreamWait;
    private String tcpStreamSubNet;
    private String tcpStreamFpgaLinkIp;
    private int emuTcpStreams;
    private String udpHost;
    private int udpPort;
    private int udpBufferSize;
    private String UdpFpgaLinkIp;
    private int udpStreams;
    private boolean isLB;
    private boolean isErsap;
    private String fileName;
    private String fileType;
    private int fileSplit;
    private int fileInternalBuffer;
    private boolean noLink;
    private boolean compression;
    private int compressionThreads;

    public DPTransport(String transportName) {
        this.transportName = transportName;
    }

    public String getTransportName() {
        return transportName;
    }

    public void setTransportName(String transportName) {
        this.transportName = transportName;
    }

    public String getTransClass() {
        return transClass;
    }

    public void setTransClass(String transClass) {
        this.transClass = transClass;
    }

    public String getEtName() {
        return etName;
    }

    public void setEtName(String etName) {
        this.etName = etName;
    }

    public boolean isEtCreate() {
        return etCreate;
    }

    public void setEtCreate(boolean etCreate) {
        this.etCreate = etCreate;
    }

    public int getEtTcpPort() {
        return etTcpPort;
    }

    public void setEtTcpPort(int etTcpPort) {
        this.etTcpPort = etTcpPort;
    }

    public int getEtUdpPort() {
        return etUdpPort;
    }

    public void setEtUdpPort(int etUdpPort) {
        this.etUdpPort = etUdpPort;
    }

    public int getEtWait() {
        return etWait;
    }

    public void setEtWait(int etWait) {
        this.etWait = etWait;
    }

    public String getmAddress() {
        return mAddress;
    }

    public void setmAddress(String mAddress) {
        this.mAddress = mAddress;
    }

    public int getEtEventNum() {
        return etEventNum;
    }

    public void setEtEventNum(int etEventNum) {
        this.etEventNum = etEventNum;
    }

    public int getEtEventSize() {
        return etEventSize;
    }

    public void setEtEventSize(int etEventSize) {
        this.etEventSize = etEventSize;
    }

    public int getInputEtChunkSize() {
        return inputEtChunkSize;
    }

    public void setInputEtChunkSize(int inputEtChunkSize) {
        this.inputEtChunkSize = inputEtChunkSize;
    }

    public int getEtChunkSize() {
        return etChunkSize;
    }

    public void setEtChunkSize(int etChunkSize) {
        this.etChunkSize = etChunkSize;
    }

    public String getSingle() {
        return single;
    }

    public void setSingle(String single) {
        this.single = single;
    }

    public int getEtGroups() {
        return etGroups;
    }

    public void setEtGroups(int etGroups) {
        this.etGroups = etGroups;
    }

    public int getEtRecvBuf() {
        return etRecvBuf;
    }

    public void setEtRecvBuf(int etRecvBuf) {
        this.etRecvBuf = etRecvBuf;
    }

    public int getEtSendBuf() {
        return etSendBuf;
    }

    public void setEtSendBuf(int etSendBuf) {
        this.etSendBuf = etSendBuf;
    }

    public String getEtMethodCon() {
        return etMethodCon;
    }

    public void setEtMethodCon(String etMethodCon) {
        this.etMethodCon = etMethodCon;
    }

    public String getEtHostName() {
        return etHostName;
    }

    public void setEtHostName(String etHostName) {
        this.etHostName = etHostName;
    }

    public String getEtSubNet() {
        return etSubNet;
    }

    public void setEtSubNet(String etSubNet) {
        this.etSubNet = etSubNet;
    }

    public String getDestinationEtCreate() {
        return destinationEtCreate;
    }

    public void setDestinationEtCreate(String destinationEtCreate) {
        this.destinationEtCreate = destinationEtCreate;
    }

    public int getEmuDirectPort() {
        return emuDirectPort;
    }

    public void setEmuDirectPort(int emuDirectPort) {
        this.emuDirectPort = emuDirectPort;
    }

    public int getEmuMaxBuffer() {
        return emuMaxBuffer;
    }

    public void setEmuMaxBuffer(int emuMaxBuffer) {
        this.emuMaxBuffer = emuMaxBuffer;
    }

    public int getEmuWait() {
        return emuWait;
    }

    public void setEmuWait(int emuWait) {
        this.emuWait = emuWait;
    }

    public String getEmuSubNet() {
        return emuSubNet;
    }

    public void setEmuSubNet(String emuSubNet) {
        this.emuSubNet = emuSubNet;
    }

    public String getFpgaLinkIp() {
        return fpgaLinkIp;
    }

    public void setFpgaLinkIp(String fpgaLinkIp) {
        this.fpgaLinkIp = fpgaLinkIp;
    }

    public boolean isEmuFatPipe() {
        return emuFatPipe;
    }

    public void setEmuFatPipe(boolean emuFatPipe) {
        this.emuFatPipe = emuFatPipe;
    }

    public int getTcpStreamDirectPort() {
        return tcpStreamDirectPort;
    }

    public void setTcpStreamDirectPort(int tcpStreamDirectPort) {
        this.tcpStreamDirectPort = tcpStreamDirectPort;
    }

    public int getTcpStreamMaxBuffer() {
        return tcpStreamMaxBuffer;
    }

    public void setTcpStreamMaxBuffer(int tcpStreamMaxBuffer) {
        this.tcpStreamMaxBuffer = tcpStreamMaxBuffer;
    }

    public int getTcpStreamWait() {
        return tcpStreamWait;
    }

    public void setTcpStreamWait(int tcpStreamWait) {
        this.tcpStreamWait = tcpStreamWait;
    }

    public String getTcpStreamSubNet() {
        return tcpStreamSubNet;
    }

    public void setTcpStreamSubNet(String tcpStreamSubNet) {
        this.tcpStreamSubNet = tcpStreamSubNet;
    }

    public String getTcpStreamFpgaLinkIp() {
        return tcpStreamFpgaLinkIp;
    }

    public void setTcpStreamFpgaLinkIp(String tcpStreamFpgaLinkIp) {
        this.tcpStreamFpgaLinkIp = tcpStreamFpgaLinkIp;
    }

    public int getEmuTcpStreams() {
        return emuTcpStreams;
    }

    public void setEmuTcpStreams(int emuTcpStreams) {
        this.emuTcpStreams = emuTcpStreams;
    }

    public String getUdpHost() {
        return udpHost;
    }

    public void setUdpHost(String udpHost) {
        this.udpHost = udpHost;
    }

    public int getUdpPort() {
        return udpPort;
    }

    public void setUdpPort(int udpPort) {
        this.udpPort = udpPort;
    }

    public int getUdpBufferSize() {
        return udpBufferSize;
    }

    public void setUdpBufferSize(int udpBufferSize) {
        this.udpBufferSize = udpBufferSize;
    }

    public String getUdpFpgaLinkIp() {
        return UdpFpgaLinkIp;
    }

    public void setUdpFpgaLinkIp(String udpFpgaLinkIp) {
        UdpFpgaLinkIp = udpFpgaLinkIp;
    }

    public int getUdpStreams() {
        return udpStreams;
    }

    public void setUdpStreams(int udpStreams) {
        this.udpStreams = udpStreams;
    }

    public boolean isLB() {
        return isLB;
    }

    public void setLB(boolean LB) {
        isLB = LB;
    }

    public boolean isErsap() {
        return isErsap;
    }

    public void setErsap(boolean ersap) {
        isErsap = ersap;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public int getFileSplit() {
        return fileSplit;
    }

    public void setFileSplit(int fileSplit) {
        this.fileSplit = fileSplit;
    }

    public int getFileInternalBuffer() {
        return fileInternalBuffer;
    }

    public void setFileInternalBuffer(int fileInternalBuffer) {
        this.fileInternalBuffer = fileInternalBuffer;
    }

    public boolean isNoLink() {
        return noLink;
    }

    public void setNoLink(boolean noLink) {
        this.noLink = noLink;
    }

    public boolean isCompression() {
        return compression;
    }

    public void setCompression(boolean compression) {
        this.compression = compression;
    }

    public int getCompressionThreads() {
        return compressionThreads;
    }

    public void setCompressionThreads(int compressionThreads) {
        this.compressionThreads = compressionThreads;
    }

    @Override
    public String toString() {
        return "DPTransport{" +
                "transportName='" + transportName + '\'' +
                ", transClass='" + transClass + '\'' +
                ", etName='" + etName + '\'' +
                ", etCreate=" + etCreate +
                ", etTcpPort=" + etTcpPort +
                ", etUdpPort=" + etUdpPort +
                ", etWait=" + etWait +
                ", mAddress='" + mAddress + '\'' +
                ", etEventNum=" + etEventNum +
                ", etEventSize=" + etEventSize +
                ", inputEtChunkSize=" + inputEtChunkSize +
                ", etChunkSize=" + etChunkSize +
                ", single='" + single + '\'' +
                ", etGroups=" + etGroups +
                ", etRecvBuf=" + etRecvBuf +
                ", etSendBuf=" + etSendBuf +
                ", etMethodCon='" + etMethodCon + '\'' +
                ", etHostName='" + etHostName + '\'' +
                ", etSubNet='" + etSubNet + '\'' +
                ", destinationEtCreate='" + destinationEtCreate + '\'' +
                ", emuDirectPort=" + emuDirectPort +
                ", emuMaxBuffer=" + emuMaxBuffer +
                ", emuWait=" + emuWait +
                ", emuSubNet='" + emuSubNet + '\'' +
                ", fpgaLinkIp='" + fpgaLinkIp + '\'' +
                ", emuFatPipe=" + emuFatPipe +
                ", tcpStreamDirectPort=" + tcpStreamDirectPort +
                ", tcpStreamMaxBuffer=" + tcpStreamMaxBuffer +
                ", tcpStreamWait=" + tcpStreamWait +
                ", tcpStreamSubNet='" + tcpStreamSubNet + '\'' +
                ", tcpStreamFpgaLinkIp='" + tcpStreamFpgaLinkIp + '\'' +
                ", emuTcpStreams=" + emuTcpStreams +
                ", udpHost='" + udpHost + '\'' +
                ", udpPort=" + udpPort +
                ", udpBufferSize=" + udpBufferSize +
                ", UdpFpgaLinkIp='" + UdpFpgaLinkIp + '\'' +
                ", udpStreams=" + udpStreams +
                ", isLB=" + isLB +
                ", isErsap=" + isErsap +
                ", fileName='" + fileName + '\'' +
                ", fileType='" + fileType + '\'' +
                ", fileSplit=" + fileSplit +
                ", fileInternalBuffer=" + fileInternalBuffer +
                ", noLink=" + noLink +
                ", compression=" + compression +
                ", compressionThreads=" + compressionThreads +
                '}';
    }
}

