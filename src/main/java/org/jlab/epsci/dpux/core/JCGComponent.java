/*
 *   Copyright (c) 2016.  Jefferson Lab (JLab). All rights reserved. Permission
 *   to use, copy, modify, and distribute  this software and its documentation for
 *   educational, research, and not-for-profit purposes, without fee and without a
 *   signed licensing agreement.
 *
 *   IN NO EVENT SHALL JLAB BE LIABLE TO ANY PARTY FOR DIRECT, INDIRECT, SPECIAL
 *   INCIDENTAL, OR CONSEQUENTIAL DAMAGES, INCLUDING LOST PROFITS, ARISING
 *   OUT OF THE USE OF THIS SOFTWARE AND ITS DOCUMENTATION, EVEN IF JLAB HAS
 *   BEEN ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 *   JLAB SPECIFICALLY DISCLAIMS ANY WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 *   THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 *   PURPOSE. THE CLARA SOFTWARE AND ACCOMPANYING DOCUMENTATION, IF ANY,
 *   PROVIDED HEREUNDER IS PROVIDED "AS IS". JLAB HAS NO OBLIGATION TO PROVIDE
 *   MAINTENANCE, SUPPORT, UPDATES, ENHANCEMENTS, OR MODIFICATIONS.
 *
 *   This software was developed under the United States Government license.
 *   For more information contact author at gurjyan@jlab.org
 *   Department of Experimental Nuclear Physics, Jefferson Lab.
 */

package org.jlab.epsci.dpux.core;


import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import java.awt.image.BufferedImage;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.StringTokenizer;

@XmlRootElement(namespace = "component")
public class JCGComponent {
    private double                                 x;
    private double                                 y;
    private double                                 gridX;
    private double                                 gridY;
    private double                                 w;
    private double                                 h;
    private double                                 px;
    private double                                 py;
    private int                                    id;
    private String                                 name                 = "";
    private String                                 type                 = "";
    private String                                 subType              = "ET";
    private String                                 rol1                 = "undefined";
    private String                                 rol1UsrString        = "undefined";
    private String                                 rol2                 = "";
    private String                                 rol2UsrString        = "";
    private String                                 description          = "undefined";
    private String                                 userConfig           = "undefined";
    private int                                    priority             = -1;
    private boolean                                codaComponent        = true;
    private boolean                                isStreaming          = false;
    private boolean                                preDefined           = false;
    private String                                 nodeList             = "undefined";
    private String                                 command              = "undefined";
    private boolean                                isMaster             = false;

    private JCGModule module;

    @XmlElementWrapper(name = "links")
    @XmlElement(name="link")
    private Set<JCGLink> linkSet
            = Collections.synchronizedSet(new HashSet<>());
    @XmlElementWrapper(name = "transports")
    @XmlElement(name="transport")
    private Set<JCGTransport> transportSet
            = Collections.synchronizedSet(new HashSet<>());
    @XmlElementWrapper(name = "processes")
    @XmlElement(name="process")
    private Set<JCGProcess> processeSet
            = Collections.synchronizedSet(new HashSet<>());
    private BufferedImage                          image;

    private String deployHost = "undefined";
    private String deployCli = "undefined";

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

    public JCGComponent(){
        module = new JCGModule();
        module.setName("Main");
    }

    public JCGComponent(String name){
        module = new JCGModule();
        module.setName("Main");
        setName(name);
    }


    public JCGComponent(double x, double y, double gridX, double gridY, double w, double h, double px, double py,
                        int id, String name, String type, String subType, String rol1, String rol1UsrString,
                        String rol2, String rol2UsrString, String description, String userConfig, int priority,
                        boolean codaComponent, boolean isStreaming, boolean preDefined, String nodeList,
                        String command, boolean isMaster, JCGModule module, Set<JCGLink> links,
                        Set<JCGTransport> transports, Set<JCGProcess> processes, BufferedImage image) {
        this.x = x;
        this.y = y;
        this.gridX = gridX;
        this.gridY = gridY;
        this.w = w;
        this.h = h;
        this.px = px;
        this.py = py;
        this.id = id;
        this.name = name;
        this.type = type;
        this.subType = subType;
        this.rol1 = rol1;
        this.rol1UsrString = rol1UsrString;
        this.rol2 = rol2;
        this.rol2UsrString = rol2UsrString;
        this.description = description;
        this.userConfig = userConfig;
        this.priority = priority;
        this.codaComponent = codaComponent;
        this.isStreaming = isStreaming;
        this.preDefined = preDefined;
        this.nodeList = nodeList;
        this.command = command;
        this.isMaster = isMaster;
        this.module = module;
        this.image = image;

        for(JCGLink l:links){
            this.linkSet.add(JCTools.deepCpLink(l));
        }

        for(JCGTransport t:transports){
            this.transportSet.add(JCTools.deepCpTransport(t));
        }

        for(JCGProcess p:processes){
            this.processeSet.add(JCTools.deepCpProcess(p));
        }
    }

    public boolean isCodaComponent() {
        return codaComponent;
    }

    public void setCodaComponent(boolean codaComponent) {
        this.codaComponent = codaComponent;
    }

    public boolean isStreaming() {
        return isStreaming;
    }

    public void setStreaming(boolean codaVersion2) {
        this.isStreaming = codaVersion2;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSubType() {
        return subType;
    }

    public void setSubType(String subType) {
        this.subType = subType;
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
        module.setId(id);
    }

    public Set<JCGProcess> getProcesses() {
        return processeSet;
    }

    public void setProcesses(Set<JCGProcess> processeSet) {
        this.processeSet = processeSet;
    }

    public void addProcess(JCGProcess p){
        removeProcess(p);
        processeSet.add(p);
    }

    public void removeProcess(JCGProcess pn){
        JCGProcess p = null;
        for(JCGProcess tmp: processeSet){
            if(tmp.getName().equals(pn.getName())){
                p = tmp;
                break;
            }
        }
        if(p!=null)
            processeSet.remove(p);
    }

    public void removeAllProcesses(){
        processeSet.clear();
    }


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public Set<JCGTransport> getTransports() {
        return transportSet;
    }

    public void setTransports(Set<JCGTransport> transportSet) {
        this.transportSet = transportSet;
    }

    public void addTransport(JCGTransport transport) {
        removeTransport(transport);
        transportSet.add(transport);
    }


    public void removeTransports(){
        if(transportSet !=null && !transportSet.isEmpty())
            transportSet.clear();
    }

    public void removeTransport(JCGTransport tn){
        JCGTransport t = null;
        for(JCGTransport tmp: transportSet){
            if(tmp.getName().equals(tn.getName())){
                t = tmp;
                break;
            }
        }
        if(t!=null)
            transportSet.remove(t);
    }

    public Set<JCGLink> getLinks() {
        return linkSet;
    }

    public void setLinks(Set<JCGLink> linkSet) {
        this.linkSet = linkSet;
    }

    public void addLink(JCGLink link) {
        removeLink(link);
        linkSet.add(link);
     }


    public void removeLinks() {
        if(linkSet !=null && !linkSet.isEmpty()){
            linkSet.clear();
        }
    }

    public void removeLink(JCGLink ln){
        JCGLink l = null;
        for(JCGLink tmp: linkSet){
            if(tmp.getName().equals(ln.getName())){
                l = tmp;
                break;
            }
        }
        if(l!=null)
            linkSet.remove(l);
    }

    public JCGModule getModule() {
        return module;
    }

    public void setModule(JCGModule module) {
        this.module = module;
    }

    public String getUserConfig() {
        return userConfig;
    }

    public void setUserConfig(String userConfig) {
        this.userConfig = userConfig;
    }

    public StringBuffer createCode(){
        StringBuffer sb = new StringBuffer();
        if(!rol1.trim().equals("")) {

            sb.append("{").append(rol1);

            sb.append(" ");

            if(!rol1UsrString.trim().equals("")) sb.append(rol1UsrString);

            sb.append("} ");
        }
        if(!rol2.trim().equals("")) {

            sb.append("{").append(rol2);

            sb.append(" ");

            if(!rol2UsrString.trim().equals("")) sb.append(rol2UsrString);

            sb.append("}");
        }

        return  sb;
    }

    public void setCode(String code){
        StringTokenizer k1,k2,k3;
        k1 = new StringTokenizer(code,"{");

        if(k1.hasMoreTokens()){
            k2 = new StringTokenizer(k1.nextToken(),"}");
            if(k2.hasMoreTokens()){
                k3 = new StringTokenizer(k2.nextToken());
                if(k3.hasMoreTokens()) rol1 = k3.nextToken();
                if(k3.hasMoreTokens()) rol1UsrString = k3.nextToken();
            }
        }
        if(k1.hasMoreTokens()){
            k2 = new StringTokenizer(k1.nextToken(),"}");
            if(k2.hasMoreTokens()){
                k3 = new StringTokenizer(k2.nextToken());
                if(k3.hasMoreTokens()) rol2 = k3.nextToken();
                if(k3.hasMoreTokens()) rol2UsrString = k3.nextToken();
            }
        }

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

    public boolean isPreDefined() {
        return preDefined;
    }

    public void setPreDefined(boolean preDefined) {
        this.preDefined = preDefined;
    }

    @XmlTransient
    public BufferedImage getImage() {
        return image;
    }
    public void setImage(BufferedImage image) {
        this.image = image;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        JCGComponent that = (JCGComponent) o;
        if (codaComponent != that.isCodaComponent()) return false;
        if (isStreaming != that.isStreaming()) return false;
        if (Double.compare(that.getH(), h) != 0) return false;
        if (id != that.id) return false;
        if (isMaster != that.isMaster) return false;
        if (priority != that.getPriority()) return false;
        if (Double.compare(that.getPx(), px) != 0) return false;
        if (Double.compare(that.getPy(), py) != 0) return false;
        if (Double.compare(that.getW(), w) != 0) return false;
        if (Double.compare(that.getX(), x) != 0) return false;
        if (Double.compare(that.getY(), y) != 0) return false;
        if (!name.equals(that.getName())) return false;
        if (!rol1.equals(that.getRol1())) return false;
        if (!rol1UsrString.equals(that.getRol1UsrString())) return false;
        if (!rol2.equals(that.getRol2())) return false;
        if (!rol2UsrString.equals(that.getRol2UsrString())) return false;
        if (!type.equals(that.getType())) return false;
        if (!module.equals(that.getModule())) return false;

        if (!userConfig.equals(that.getUserConfig())) return false;
        if (!description.equals(that.getDescription())) return false;


        if(linkSet.size()!=that.getLinks().size()) return false;
        else {
            for(JCGLink t_this: linkSet){
                boolean f = false;
                for(JCGLink t_that:that.getLinks()){
                    if(t_this.equals(t_that)){
                        f = true;
                        break;
                    }
                }
                if(!f) return false;
            }
        }

        if(processeSet.size()!=that.getProcesses().size()) return false;
        else {
            for(JCGProcess t_this: processeSet){
                boolean f = false;
                for(JCGProcess t_that:that.getProcesses()){
                    if(t_this.equals(t_that)){
                        f = true;
                        break;
                    }
                }
                if(!f) return false;
            }
        }

        if(transportSet.size()!=that.getTransports().size()) return false;
        else {
             for(JCGTransport t_this: transportSet){
                 boolean f = false;
                 for(JCGTransport t_that:that.getTransports()){
                     if(t_this.equals(t_that)){
                         f = true;
                         break;
                     }
                 }
                 if(!f) return false;
             }
        }
        return true;
    }

    @Override
    public String toString() {
        return "JCGComponent{" +
                "x=" + x +
                ", y=" + y +
                ", gridX=" + gridX +
                ", gridY=" + gridY +
                ", w=" + w +
                ", h=" + h +
                ", px=" + px +
                ", py=" + py +
                ", id=" + id +
                ", name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", subType='" + subType + '\'' +
                ", rol1='" + rol1 + '\'' +
                ", rol1UsrString='" + rol1UsrString + '\'' +
                ", rol2='" + rol2 + '\'' +
                ", rol2UsrString='" + rol2UsrString + '\'' +
                ", description='" + description + '\'' +
                ", userConfig='" + userConfig + '\'' +
                ", priority=" + priority +
                ", codaComponent=" + codaComponent +
                ", isStreaming=" + isStreaming +
                ", preDefined=" + preDefined +
                ", nodeList='" + nodeList + '\'' +
                ", command='" + command + '\'' +
                ", isMaster=" + isMaster +
                ", module=" + module +
                ", links=" + linkSet +
                ", transports=" + transportSet +
                ", processes=" + processeSet +
                ", image=" + image +
                ", deployHost='" + deployHost + '\'' +
                ", deployCli='" + deployCli + '\'' +
                '}';
    }
}
