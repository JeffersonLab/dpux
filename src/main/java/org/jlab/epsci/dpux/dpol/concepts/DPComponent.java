package org.jlab.epsci.dpux.dpol.concepts;

import java.util.List;

public class DPComponent {
    private double x;
    private double y;
    private double gridX;
    private double gridY;
    private double w;
    private double h;
    private double px;
    private double py;
    private int id;
    private String componentName;
    private String type;
    private String subType;
    private String rol1;
    private String rol1UsrString;
    private String rol2;
    private String rol2UsrString;
    private String description;
    private String userConfig;
    private int priority;
    private boolean isStreaming;
    private boolean preDefined;
    private String nodeList;
    private String command;
    private boolean isMaster;
    private String deployHost;
    private String deployCli;
    private List<DPProcess> processes;  // List of processes associated with the component
    private List<DPLink> links;        // List of links associated with the component

    // Constructor
    public DPComponent(String componentName) {
        this.componentName = componentName;
    }

    // Getters and Setters
    public String getComponentName() {
        return componentName;
    }

    public void setComponentName(String componentName) {
        this.componentName = componentName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getGridX() {
        return gridX;
    }

    public void setGridX(double gridX) {
        this.gridX = gridX;
    }

    public double getGridY() {
        return gridY;
    }

    public void setGridY(double gridY) {
        this.gridY = gridY;
    }

    public double getW() {
        return w;
    }

    public void setW(double w) {
        this.w = w;
    }

    public double getH() {
        return h;
    }

    public void setH(double h) {
        this.h = h;
    }

    public double getPx() {
        return px;
    }

    public void setPx(double px) {
        this.px = px;
    }

    public double getPy() {
        return py;
    }

    public void setPy(double py) {
        this.py = py;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSubType() {
        return subType;
    }

    public void setSubType(String subType) {
        this.subType = subType;
    }

    public String getRol1() {
        return rol1;
    }

    public void setRol1(String rol1) {
        this.rol1 = rol1;
    }

    public String getRol1UsrString() {
        return rol1UsrString;
    }

    public void setRol1UsrString(String rol1UsrString) {
        this.rol1UsrString = rol1UsrString;
    }

    public String getRol2() {
        return rol2;
    }

    public void setRol2(String rol2) {
        this.rol2 = rol2;
    }

    public String getRol2UsrString() {
        return rol2UsrString;
    }

    public void setRol2UsrString(String rol2UsrString) {
        this.rol2UsrString = rol2UsrString;
    }

    public String getUserConfig() {
        return userConfig;
    }

    public void setUserConfig(String userConfig) {
        this.userConfig = userConfig;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public boolean isStreaming() {
        return isStreaming;
    }

    public void setStreaming(boolean streaming) {
        isStreaming = streaming;
    }

    public boolean isPreDefined() {
        return preDefined;
    }

    public void setPreDefined(boolean preDefined) {
        this.preDefined = preDefined;
    }

    public String getNodeList() {
        return nodeList;
    }

    public void setNodeList(String nodeList) {
        this.nodeList = nodeList;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public boolean isMaster() {
        return isMaster;
    }

    public void setMaster(boolean master) {
        isMaster = master;
    }

    public String getDeployHost() {
        return deployHost;
    }

    public void setDeployHost(String deployHost) {
        this.deployHost = deployHost;
    }

    public String getDeployCli() {
        return deployCli;
    }

    public void setDeployCli(String deployCli) {
        this.deployCli = deployCli;
    }

    public List<DPProcess> getProcesses() {
        return processes;
    }

    public void setProcesses(List<DPProcess> processes) {
        this.processes = processes;
    }

    public List<DPLink> getLinks() {
        return links;
    }

    public void setLinks(List<DPLink> links) {
        this.links = links;
    }

    // ToString method for debugging or printing the object

    @Override
    public String toString() {
        return "DPComponent{" +
                "x=" + x +
                ", y=" + y +
                ", gridX=" + gridX +
                ", gridY=" + gridY +
                ", w=" + w +
                ", h=" + h +
                ", px=" + px +
                ", py=" + py +
                ", id=" + id +
                ", name='" + componentName + '\'' +
                ", type='" + type + '\'' +
                ", subType='" + subType + '\'' +
                ", rol1='" + rol1 + '\'' +
                ", rol1UsrString='" + rol1UsrString + '\'' +
                ", rol2='" + rol2 + '\'' +
                ", rol2UsrString='" + rol2UsrString + '\'' +
                ", description='" + description + '\'' +
                ", userConfig='" + userConfig + '\'' +
                ", priority=" + priority +
                ", isStreaming=" + isStreaming +
                ", preDefined=" + preDefined +
                ", nodeList='" + nodeList + '\'' +
                ", command='" + command + '\'' +
                ", isMaster=" + isMaster +
                ", deployHost='" + deployHost + '\'' +
                ", deployCli='" + deployCli + '\'' +
                '}';
    }
}
