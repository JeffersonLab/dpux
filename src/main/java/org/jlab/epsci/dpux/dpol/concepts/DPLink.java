package org.jlab.epsci.dpux.dpol.concepts;

public class DPLink {
    private String sourceEndian;
    private int sourceCapacity;
    private int sourceGroup;
    private String sourceStationName;
    private int sourcePosition;
    private boolean sourceIdFilter;
    private int sourceSendBuffer;
    private int sourceRecvBuffer;
    private boolean sourceNoDelay;
    private int sourceOThreads;
    private int sourceWThreads;
    private String sourceTransportName;
    private String sourceModuleName;
    private String sourceComponentName;
    private String destinationEndian;
    private int destinationCapacity;
    private int destinationGroup;
    private String destinationStationName;
    private int destinationPosition;
    private boolean destinationIdFilter;
    private int destinationSendBuffer;
    private int destinationRecvBuffer;
    private boolean destinationNoDelay;
    private int destinationIThreads;
    private String destinationTransportName;
    private String destinationModuleName;
    private String destinationComponentName;
    private String linkName;
    private int startX;
    private int endX;
    private int startY;
    private int endY;
    private DPComponent sourceComponent;  // Source component of the link
    private DPComponent targetComponent;  // Target component of the link

    public DPLink(String linkName) {
        this.linkName = linkName;
    }

    public String getSourceEndian() {
        return sourceEndian;
    }

    public void setSourceEndian(String sourceEndian) {
        this.sourceEndian = sourceEndian;
    }

    public int getSourceCapacity() {
        return sourceCapacity;
    }

    public void setSourceCapacity(int sourceCapacity) {
        this.sourceCapacity = sourceCapacity;
    }

    public int getSourceGroup() {
        return sourceGroup;
    }

    public void setSourceGroup(int sourceGroup) {
        this.sourceGroup = sourceGroup;
    }

    public String getSourceStationName() {
        return sourceStationName;
    }

    public void setSourceStationName(String sourceStationName) {
        this.sourceStationName = sourceStationName;
    }

    public int getSourcePosition() {
        return sourcePosition;
    }

    public void setSourcePosition(int sourcePosition) {
        this.sourcePosition = sourcePosition;
    }

    public boolean isSourceIdFilter() {
        return sourceIdFilter;
    }

    public void setSourceIdFilter(boolean sourceIdFilter) {
        this.sourceIdFilter = sourceIdFilter;
    }

    public int getSourceSendBuffer() {
        return sourceSendBuffer;
    }

    public void setSourceSendBuffer(int sourceSendBuffer) {
        this.sourceSendBuffer = sourceSendBuffer;
    }

    public int getSourceRecvBuffer() {
        return sourceRecvBuffer;
    }

    public void setSourceRecvBuffer(int sourceRecvBuffer) {
        this.sourceRecvBuffer = sourceRecvBuffer;
    }

    public boolean isSourceNoDelay() {
        return sourceNoDelay;
    }

    public void setSourceNoDelay(boolean sourceNoDelay) {
        this.sourceNoDelay = sourceNoDelay;
    }

    public int getSourceOThreads() {
        return sourceOThreads;
    }

    public void setSourceOThreads(int sourceOThreads) {
        this.sourceOThreads = sourceOThreads;
    }

    public int getSourceWThreads() {
        return sourceWThreads;
    }

    public void setSourceWThreads(int sourceWThreads) {
        this.sourceWThreads = sourceWThreads;
    }

    public String getSourceTransportName() {
        return sourceTransportName;
    }

    public void setSourceTransportName(String sourceTransportName) {
        this.sourceTransportName = sourceTransportName;
    }

    public String getSourceModuleName() {
        return sourceModuleName;
    }

    public void setSourceModuleName(String sourceModuleName) {
        this.sourceModuleName = sourceModuleName;
    }

    public String getSourceComponentName() {
        return sourceComponentName;
    }

    public void setSourceComponentName(String sourceComponentName) {
        this.sourceComponentName = sourceComponentName;
    }

    public String getDestinationEndian() {
        return destinationEndian;
    }

    public void setDestinationEndian(String destinationEndian) {
        this.destinationEndian = destinationEndian;
    }

    public int getDestinationCapacity() {
        return destinationCapacity;
    }

    public void setDestinationCapacity(int destinationCapacity) {
        this.destinationCapacity = destinationCapacity;
    }

    public int getDestinationGroup() {
        return destinationGroup;
    }

    public void setDestinationGroup(int destinationGroup) {
        this.destinationGroup = destinationGroup;
    }

    public String getDestinationStationName() {
        return destinationStationName;
    }

    public void setDestinationStationName(String destinationStationName) {
        this.destinationStationName = destinationStationName;
    }

    public int getDestinationPosition() {
        return destinationPosition;
    }

    public void setDestinationPosition(int destinationPosition) {
        this.destinationPosition = destinationPosition;
    }

    public boolean isDestinationIdFilter() {
        return destinationIdFilter;
    }

    public void setDestinationIdFilter(boolean destinationIdFilter) {
        this.destinationIdFilter = destinationIdFilter;
    }

    public int getDestinationSendBuffer() {
        return destinationSendBuffer;
    }

    public void setDestinationSendBuffer(int destinationSendBuffer) {
        this.destinationSendBuffer = destinationSendBuffer;
    }

    public int getDestinationRecvBuffer() {
        return destinationRecvBuffer;
    }

    public void setDestinationRecvBuffer(int destinationRecvBuffer) {
        this.destinationRecvBuffer = destinationRecvBuffer;
    }

    public boolean isDestinationNoDelay() {
        return destinationNoDelay;
    }

    public void setDestinationNoDelay(boolean destinationNoDelay) {
        this.destinationNoDelay = destinationNoDelay;
    }

    public int getDestinationIThreads() {
        return destinationIThreads;
    }

    public void setDestinationIThreads(int destinationIThreads) {
        this.destinationIThreads = destinationIThreads;
    }

    public String getDestinationTransportName() {
        return destinationTransportName;
    }

    public void setDestinationTransportName(String destinationTransportName) {
        this.destinationTransportName = destinationTransportName;
    }

    public String getDestinationModuleName() {
        return destinationModuleName;
    }

    public void setDestinationModuleName(String destinationModuleName) {
        this.destinationModuleName = destinationModuleName;
    }

    public String getDestinationComponentName() {
        return destinationComponentName;
    }

    public void setDestinationComponentName(String destinationComponentName) {
        this.destinationComponentName = destinationComponentName;
    }

    public String getLinkName() {
        return linkName;
    }

    public void setLinkName(String linkName) {
        this.linkName = linkName;
    }

    public int getStartX() {
        return startX;
    }

    public void setStartX(int startX) {
        this.startX = startX;
    }

    public int getEndX() {
        return endX;
    }

    public void setEndX(int endX) {
        this.endX = endX;
    }

    public int getStartY() {
        return startY;
    }

    public void setStartY(int startY) {
        this.startY = startY;
    }

    public int getEndY() {
        return endY;
    }

    public void setEndY(int endY) {
        this.endY = endY;
    }

    public DPComponent getSourceComponent() {
        return sourceComponent;
    }

    public void setSourceComponent(DPComponent sourceComponent) {
        this.sourceComponent = sourceComponent;
    }

    public DPComponent getTargetComponent() {
        return targetComponent;
    }

    public void setTargetComponent(DPComponent targetComponent) {
        this.targetComponent = targetComponent;
    }

    @Override
    public String toString() {
        return "DPLink{" +
                "sourceEndian='" + sourceEndian + '\'' +
                ", sourceCapacity=" + sourceCapacity +
                ", sourceGroup=" + sourceGroup +
                ", sourceStationName='" + sourceStationName + '\'' +
                ", sourcePosition=" + sourcePosition +
                ", sourceIdFilter=" + sourceIdFilter +
                ", sourceSendBuffer=" + sourceSendBuffer +
                ", sourceRecvBuffer=" + sourceRecvBuffer +
                ", sourceNoDelay=" + sourceNoDelay +
                ", sourceOThreads=" + sourceOThreads +
                ", sourceWThreads=" + sourceWThreads +
                ", sourceTransportName='" + sourceTransportName + '\'' +
                ", sourceModuleName='" + sourceModuleName + '\'' +
                ", sourceComponentName='" + sourceComponentName + '\'' +
                ", destinationEndian='" + destinationEndian + '\'' +
                ", destinationCapacity=" + destinationCapacity +
                ", destinationGroup=" + destinationGroup +
                ", destinationStationName='" + destinationStationName + '\'' +
                ", destinationPosition=" + destinationPosition +
                ", destinationIdFilter=" + destinationIdFilter +
                ", destinationSendBuffer=" + destinationSendBuffer +
                ", destinationRecvBuffer=" + destinationRecvBuffer +
                ", destinationNoDelay=" + destinationNoDelay +
                ", destinationIThreads=" + destinationIThreads +
                ", destinationTransportName='" + destinationTransportName + '\'' +
                ", destinationModuleName='" + destinationModuleName + '\'' +
                ", destinationComponentName='" + destinationComponentName + '\'' +
                ", linkName='" + linkName + '\'' +
                ", startX=" + startX +
                ", endX=" + endX +
                ", startY=" + startY +
                ", endY=" + endY +
                ", sourceComponent=" + sourceComponent.getComponentName() +
                ", targetComponent=" + targetComponent.getComponentName() +
                '}';
    }
}
