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

package org.jlab.epsci.dpux.user;

import org.jlab.epsci.dpux.desktop.CDesktopNew;
import org.jlab.epsci.dpux.core.*;
import org.jlab.epsci.dpux.util.JCUtil;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class LLConfigWriter {
    private String filePath;
    private Collection<JCGComponent> components;
    private HashMap<String, ExternalConfig>
            _compDat = new HashMap<>();
    private HashMap<String, JCGComponent> _compMap = new HashMap<>();
    private JCGSetup stp = JCGSetup.getInstance();


    /**
     * Constructor
     *
     * @param runType    configuration
     * @param components list of all components on the canvas
     */
    public LLConfigWriter(String runType, Collection<JCGComponent> components) {
        this.components = components;
        filePath = stp.getCoolHome() + File.separator +
                stp.getExpid() + File.separator +
                "config" + File.separator +
                "Control" + File.separator +
                runType + File.separator +
                "Options" + File.separator;
        for (JCGComponent c : components) {
            _compMap.put(c.getName(), c);
        }

    }

//    /**
//     * Constructor
//     * @param runType configuration
//     * @param component JCGComponent object
//     */
//    public LLConfigWriter(String runType, JCGComponent component) {
//        components = new ArrayList<JCGComponent>();
//        components.add(component);
//        filePath = stp.getCoolHome() + File.separator +
//                stp.getExpid() + File.separator +
//                "config" + File.separator +
//                "Control" + File.separator +
//                runType + File.separator +
//                "Options" + File.separator;
//
//    }


    /**
     * Main processing routine
     */
    public void process() {
        for (JCGComponent component : components) {
            processComponent(component);
        }
        flush2file();
    }

    /**
     * Fills local map with the information of a required component
     *
     */
    private void processComponent(JCGComponent cmp) {
        ExternalConfig dCfg, sCfg;
        int group = 1;

        // this method is called for each component that might be linked
        // to a component that had already recorded it's information in
        // the map. Retrieve it from a map and add more information
        if (_compDat.containsKey(cmp.getName())) {
            dCfg = _compDat.get(cmp.getName());
        } else {
            dCfg = new ExternalConfig();
            dCfg.setName(cmp.getName());
            dCfg.setType(cmp.getType());
            dCfg.setStreaming(cmp.isStreaming());
        }

        // get component input links and get destination transports and channels
        // i.e. transports and input channels for this component
        for (JCGLink l : getInputLinks(cmp)) {

            JCGTransport tr = getDestinationTransport(l);
            if (tr != null) {
                dCfg.addTransport(tr);
            }
            JCGChannel ch = getDestinationChannel(l);
            if (ch != null) {
                dCfg.addInputChannel(ch);
            }


            // for the source component of this link define output channel and
            // add a new linkedComponent transport to it
            JCGChannel sc = getSourceChannel(l);
            if (sc != null) {
                sc.setGroup(group);
                if (_compDat.containsKey(l.getSourceComponentName())) {
                    sCfg = _compDat.get(l.getSourceComponentName());
                } else {
                    sCfg = new ExternalConfig();
                    sCfg.setName(l.getSourceComponentName());
                    if (_compMap.get(l.getSourceComponentName()) != null) {
                        sCfg.setType(_compMap.get(l.getSourceComponentName()).getType());
                    }
                }

                if (getDestinationTransport(l) != null) {
                    JCGTransport destinationTransport = getDestinationTransport(l);
                    JCGTransport linkedComponentTransport = JCUtil.deepCpTransport(destinationTransport.getName(), false, destinationTransport);
                    sCfg.addTransport(linkedComponentTransport);
                }
                // add channel
                sCfg.addOutputChannel(sc);

                JCGTransport str = getSourceTransport(l);
                if (str != null) {
                    sCfg.setFat(str.isEmuFatPipe());
                }

                _compDat.put(sCfg.getName(), sCfg);
            }
            group++;
        }

        _compDat.put(dCfg.getName(), dCfg);
    }

    /**
     * Creates configuration files
     */
    private void flush2file() {
        String fileName;
        for (ExternalConfig ec : _compDat.values()) {
//            System.out.println("DDD component name = "+ec.getName());

            // farm control system component config creation
            if (ec.getType() != null) {
                fileName = filePath + ec.getName() + ".xml";

                if (ec.getType().equals(ACodaType.ROC.name()) ||
                        ec.getType().equals(ACodaType.USR.name()) ||
                        ec.getType().equals(ACodaType.GT.name()) ||
                        ec.getType().equals(ACodaType.FPGA.name()) ||
                        ec.getType().equals(ACodaType.TS.name())) {
                    createRocConfigFile(ec.getName());
                }

                try {
                    BufferedWriter out = new BufferedWriter(new FileWriter(fileName));
                    out.write("<?xml version=\"1.0\"?>\n");

                    out.write("<component name=\"" + ec.getName() + "\">\n\n");

                    // transports
                    out.write("   <transports>\n\n");
                    int nl = ec.getiChannels().size();
//                    ArrayList<String> tpNames = new ArrayList<>();
                    Set<String> tpNames = new HashSet<>(); // vg 01.28.22
                    for (JCGTransport tr : ec.getTransports()) {
                        // avoid writing the same transport twice.
                        String tName = tr.getName();
                        if (!tpNames.contains(tName)) {
                            out.write(writeTransport(ec, tr, nl, ec.isStreaming()));
                            tpNames.add(tName);
                        }
                    }
                    out.write("   </transports>\n\n");

                    JCGComponent cmp = null;
                    for (JCGComponent c : components) {
                        if (c.getName().equals(ec.getName())) {
                            cmp = c;
                            break;
                        }
                    }

                    if (cmp.getType().equals(ACodaType.EBER.name())) {
                        out.write(writeEBERModulesAndChannels(cmp));
                        for (JCGChannel ch : ec.getoChannels().values()) {
                            out.write(writeOutChannels(ch, ec.isFat, cmp.getId()));
                        }
                        out.append("     </ErModule>\n\n");
                    } else {
                        // modules
                        out.write(writeModule(cmp));

                        // find the component
                        // channels
                        for (JCGChannel ch : ec.getiChannels().values()) {
                            out.write(writeInChannels(ec.getName(), ec.getType(), ch));
                        }
                        for (JCGChannel ch : ec.getoChannels().values()) {
                            out.write(writeOutChannels(ch, ec.isFat, cmp.getId()));
                        }
                        if (ec.getType().equals(ACodaType.ER.name())) {
                            out.write("     </ErModule>\n\n");
                        } else if (ec.getType().equals(ACodaType.ROC.name())) {
                            out.write("     </RocModule>\n\n");
                        } else if (ec.getType().equals(ACodaType.GT.name())) {
                            out.write("     </GTriggerModule>\n\n");
                        } else if (ec.getType().equals(ACodaType.FPGA.name())) {
                            out.write("     </FPGATriggerModule>\n\n");
                        } else if (ec.getType().equals(ACodaType.USR.name())) {
                            out.write("     </UsrModule>\n\n");
                        } else if (ec.getType().equals(ACodaType.TS.name())) {
                            out.write("     </TsModule>\n\n");
                        } else {
                            out.write("     </EbModule>\n\n");
                        }
                    }
                    out.write("   </modules>\n\n");

                    out.write("</component>\n\n");
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
//                }
//                serializeDumpComponent(ec.getName());
            }
        }
    }

    private String writeTransport(ExternalConfig ec, JCGTransport tr, int nl, boolean isStreaming) {
        String cName = ec.getName();
        StringBuilder out = new StringBuilder();

        switch (tr.getTransClass()) {

            case "Et":
                tr.setEtCreate((tr.getName().equals((cName + "_transport"))) && (tr.getDestinationEtCreate().equals("true")));
                int etEvtMin1 = (nl * 2 * tr.getEtChunkSize()) * 2;
                int etEvtMin2 = (tr.getInputEtChunkSize() * 4) * 2;

                if (etEvtMin1 >= etEvtMin2) {
                    if (tr.getEtEventNum() < etEvtMin1) {
                        tr.setEtEventNum(etEvtMin1);
                        JCGComponent c = CDesktopNew.getDrawingCvanvas().getGCMPs().get(cName);
                        for (JCGTransport t : c.getTransports()) {
                            if (t.getName().equals(tr.getName())) {
                                t.setEtEventNum(tr.getEtEventNum());
                                break;
                            }
                        }
                    }
                } else {
                    if (tr.getEtEventNum() < etEvtMin2) {
                        tr.setEtEventNum(etEvtMin2);
                        JCGComponent c = CDesktopNew.getDrawingCvanvas().getGCMPs().get(cName);
                        for (JCGTransport t : c.getTransports()) {
                            if (t.getName().equals(tr.getName())) {
                                t.setEtEventNum(tr.getEtEventNum());
                                break;
                            }
                        }
                    }
                }
                if (tr.isEtCreate()) {
                    out.append("     <server name=\"" + tr.getName() + "\" " +
                            "streaming=\"" + "on" + "\" " +
                            "class=\"Et\" " +
                            "etName=\"" + tr.getEtName() + "\" " +
                            "create=\"" + tr.getDestinationEtCreate() + "\" " +
                            "uPort=\"" + tr.getEtUdpPort() + "\" " +
                            "port=\"" + tr.getEtTcpPort() + "\" " +
                            "mAddr=\"" + tr.getmAddress() + "\" " +
                            "eventNum=\"" + tr.getEtEventNum() + "\" " +
                            "eventSize=\"" + tr.getEtEventSize() + "\" " +
                            "groups=\"" + nl + "\" " +
//                        "wait=\"" + tr.getEtWait() + "\" " +
                            "/>\n\n");
                } else {
                    if (tr.getEtSubNet().equals("undefined") || tr.getEtSubNet().equals("")) {
                        out.append("     <server name=\"" + tr.getName() + "\" " +
                                "class=\"Et\" " +
                                "etName=\"" + tr.getEtName() + "\" " + "" +
                                "method=\"" + tr.getEtMethodCon() + "\" " +
                                "host=\"" + tr.getEtHostName() + "\" " +
                                "port=\"" + tr.getEtTcpPort() + "\" " +
                                "uPort=\"" + tr.getEtUdpPort() + "\" " +
                                "wait=\"" + tr.getEtWait() + "\"" +
                                "/>\n\n");
                    } else {
                        out.append("     <server name=\"" + tr.getName() + "\" " +
                                "class=\"Et\" " +
                                "etName=\"" + tr.getEtName() + "\" " + "" +
                                "method=\"" + tr.getEtMethodCon() + "\" " +
                                "host=\"" + tr.getEtHostName() + "\" " +
                                "port=\"" + tr.getEtTcpPort() + "\" " +
                                "uPort=\"" + tr.getEtUdpPort() + "\" " +
                                "subnet=\"" + tr.getEtSubNet() + "\" " +
                                "wait=\"" + tr.getEtWait() + "\"" +
                                "/>\n\n");
                    }
                }
                break;

            case "EmuSocket":
                if (tr.getName().equals((cName + "_transport"))) {
                    out.append("     <client name=\"" + tr.getName() + "\" " +
                            "class=\"Emu\" " +
                            "port=\"" + tr.getEmuDirectPort() + "\" " +
                            "/>\n\n");
                } else {
                    out.append("     <server name=\"" + tr.getName() + "\" " +
                            "class=\"Emu\" " +
                            "/>\n\n");
                }
                break;

            case "EmuSocket+Et":
                if (ec.getType().equals(ACodaType.ER.name()) ||
                        ec.getType().equals(ACodaType.EBER.name())) {
                    // ET
                    tr.setEtCreate((tr.getName().equals((cName + "_transport"))) && (tr.getDestinationEtCreate().equals("true")));

                    etEvtMin1 = (nl * 2 * tr.getEtChunkSize()) * 2;
                    etEvtMin2 = (tr.getInputEtChunkSize() * 4) * 2;

                    if (etEvtMin1 >= etEvtMin2) {
                        if (tr.getEtEventNum() < etEvtMin1) {
                            tr.setEtEventNum(etEvtMin1);
                            JCGComponent c = CDesktopNew.getDrawingCvanvas().getGCMPs().get(cName);
                            for (JCGTransport t : c.getTransports()) {
                                if (t.getName().equals(tr.getName())) {
                                    t.setEtEventNum(tr.getEtEventNum());
                                    break;
                                }
                            }
                        }
                    } else {
                        if (tr.getEtEventNum() < etEvtMin2) {
                            tr.setEtEventNum(etEvtMin2);
                            JCGComponent c = CDesktopNew.getDrawingCvanvas().getGCMPs().get(cName);
                            for (JCGTransport t : c.getTransports()) {
                                if (t.getName().equals(tr.getName())) {
                                    t.setEtEventNum(tr.getEtEventNum());
                                    break;
                                }
                            }
                        }
                    }
                    if (tr.isEtCreate()) {
                        out.append("     <server name=\"" + tr.getName() + "_async" + "\" " +
                                "class=\"Et\" " +
                                "etName=\"" + tr.getEtName() + "\" " +
                                "create=\"" + tr.getDestinationEtCreate() + "\" " +
                                "uPort=\"" + tr.getEtUdpPort() + "\" " +
                                "port=\"" + tr.getEtTcpPort() + "\" " +
                                "mAddr=\"" + tr.getmAddress() + "\" " +
                                "eventNum=\"" + tr.getEtEventNum() + "\" " +
                                "eventSize=\"" + tr.getEtEventSize() + "\" " +
                                "groups=\"" + nl + "\" " +
//                        "wait=\"" + tr.getEtWait() + "\" " +
                                "/>\n\n");
                    } else {
                        if (tr.getEtSubNet().equals("undefined") || tr.getEtSubNet().equals("")) {
                            out.append("     <server name=\"" + tr.getName() + "_async" + "\" " +
                                    "class=\"Et\" " +
                                    "etName=\"" + tr.getEtName() + "\" " + "" +
                                    "method=\"" + tr.getEtMethodCon() + "\" " +
                                    "host=\"" + tr.getEtHostName() + "\" " +
                                    "port=\"" + tr.getEtTcpPort() + "\" " +
                                    "uPort=\"" + tr.getEtUdpPort() + "\" " +
                                    "wait=\"" + tr.getEtWait() + "\"" +
                                    "/>\n\n");
                        } else {
                            out.append("     <server name=\"" + tr.getName() + "_async" + "\" " +
                                    "class=\"Et\" " +
                                    "etName=\"" + tr.getEtName() + "\" " + "" +
                                    "method=\"" + tr.getEtMethodCon() + "\" " +
                                    "host=\"" + tr.getEtHostName() + "\" " +
                                    "port=\"" + tr.getEtTcpPort() + "\" " +
                                    "uPort=\"" + tr.getEtUdpPort() + "\" " +
                                    "subnet=\"" + tr.getEtSubNet() + "\" " +
                                    "wait=\"" + tr.getEtWait() + "\"" +
                                    "/>\n\n");
                        }
                    }
                }
                break;

            case "TcpStream":
                if (tr.getName().equals((cName + "_transport"))) {
                    out.append("     <client name=\"" + tr.getName() + "\" " +
                            "streaming=\"" + "on" + "\" " +
                            "class=\"TcpStream\" " +
                            "port=\"" + tr.getTcpStreamDirectPort() + "\" " +
                            "/>\n\n");
                } else {
                    out.append("     <server name=\"" + tr.getName() + "\" " +
                            "streaming=\"" + "on" + "\" " +
                            "class=\"TcpStream\" " +
                            "/>\n\n");
                }

                break;

            case "UdpStream":
                boolean isInChannel = false;
                boolean isOutChannel = false;

                for(JCGChannel ch: ec.getiChannels().values()){
                    if(ch.getTransport().getName().equals(tr.getName())){
                        isInChannel = true;
                        break;
                    } else if(ch.getTransport().getName().equals(tr.getName())){
                        isOutChannel = true;
                        break;
                    }
                }
                if (isInChannel) {
                    if (tr.getName().equals((cName + "_transport"))) {
                        out.append("     <client name=\"" + tr.getName() + "\" " +
                                "streaming=\"" + "on" + "\" " +
                                "class=\"UdpStream\" " +
                                "port=\"" + tr.getUdpPort() + "\" " +
                                "/>\n\n");
                    } else {
                        out.append("     <client name=\"" + tr.getName() + "\" " +
                                "streaming=\"" + "on" + "\" " +
                                "class=\"UdpStream\"" +
                                "/>\n\n");
                    }
                } else if (isOutChannel){
                    if (tr.getName().equals((cName + "_transport"))) {
                        out.append("     <client name=\"" + tr.getName() + "\" " +
                                "streaming=\"" + "on" + "\" " +
                                "class=\"UdpStream\" " +
                                "port=\"" + tr.getUdpPort() + "\" " +
                                "/>\n\n");
                    } else {
                        out.append("     <server name=\"" + tr.getName() + "\" " +
                                "streaming=\"" + "on" + "\" " +
                                "class=\"UdpStream\"" +
                                "/>\n\n");
                    }
                } else {
                    if (tr.getName().equals((cName + "_transport"))) {
                        out.append("     <client name=\"" + tr.getName() + "\" " +
                                "streaming=\"" + "on" + "\" " +
                                "class=\"UdpStream\" " +
                                "port=\"" + tr.getUdpPort() + "\" " +
                                "/>\n\n");
                    } else {
                        out.append("     <server name=\"" + tr.getName() + "\" " +
                                "streaming=\"" + "on" + "\" " +
                                "class=\"UdpStream\"" +
                                "/>\n\n");
                    }
                }
                break;

            case "File":
                out.append("     <server name=\"" + tr.getName() + "\" " +
                        "class=\"File\" " +
                        "/>\n\n");
                break;
        }
        return out.toString();
    }


    private String writeEBERModulesAndChannels(JCGComponent cmp) {
        StringBuilder out = new StringBuilder();
        JCGModule md;
        boolean isEndianLittle = false;

        md = cmp.getModule();
        if (md != null) {

            for (JCGChannel ch : md.getChnnels()) {
                if (ch.getEndian().equals("little")) {
                    isEndianLittle = true;
                    break;
                }
            }
            out.append("   <modules>\n\n");
            if (cmp.getType().equals(ACodaType.EBER.name())) {
                // EB module =============================================
                if (cmp.isStreaming()) {
                    if (isEndianLittle) {
                        out.append("     <EbModule class=\"" + "Aggregator" + "\" " +
                                "streaming=\"" + "on" + "\" " +
                                "id=\"" + md.getId() + "\" " +
                                "threads=\"" + md.getThreads() + "\" " +
                                "timeStats=\"off\" " +
                                "runData=\"" + md.isRunData() + "\" " +
                                "tsCheck=\"" + md.isTsCheck() + "\" " +
                                "tsSlop=\"" + md.getTsSlop() + "\" " +
                                "sparsify=\"" + md.isSparsify() + "\" " +
                                "endian=\"" + "little" + "\"" +
                                "> \n\n");
                    } else {
                        out.append("     <EbModule class=\"" + "Aggregator" + "\" " +
                                "streaming=\"" + "on" + "\" " +
                                "id=\"" + md.getId() + "\" " +
                                "threads=\"" + md.getThreads() + "\" " +
                                "timeStats=\"off\" " +
                                "runData=\"" + md.isRunData() + "\" " +
                                "tsCheck=\"" + md.isTsCheck() + "\" " +
                                "tsSlop=\"" + md.getTsSlop() + "\" " +
                                "sparsify=\"" + md.isSparsify() + "\"" +
                                "> \n\n");
                    }
                } else {
                    if (isEndianLittle) {
                        out.append("     <EbModule class=\"" + md.getModuleClass(ACodaType.EB.name()) + "\" " +
                                "id=\"" + md.getId() + "\" " +
                                "threads=\"" + md.getThreads() + "\" " +
                                "timeStats=\"off\" " +
                                "runData=\"" + md.isRunData() + "\" " +
                                "tsCheck=\"" + md.isTsCheck() + "\" " +
                                "tsSlop=\"" + md.getTsSlop() + "\" " +
                                "sparsify=\"" + md.isSparsify() + "\" " +
                                "endian=\"" + "little" + "\"" +
                                "> \n\n");
                    } else {
                        out.append("     <EbModule class=\"" + md.getModuleClass(ACodaType.EB.name()) + "\" " +
                                "id=\"" + md.getId() + "\" " +
                                "threads=\"" + md.getThreads() + "\" " +
                                "timeStats=\"off\" " +
                                "runData=\"" + md.isRunData() + "\" " +
                                "tsCheck=\"" + md.isTsCheck() + "\" " +
                                "tsSlop=\"" + md.getTsSlop() + "\" " +
                                "sparsify=\"" + md.isSparsify() + "\"" +
                                "> \n\n");
                    }
                }

                // input channel for EB
                ExternalConfig ec = _compDat.get(cmp.getName());
                for (JCGChannel ch : ec.getiChannels().values()) {
                    out.append("         <inchannel id=\"" + ch.getId() + "\" " +
                            "name=\"" + ch.getName() + "\" " +
                            "transp=\"" + ch.getTransport().getName() + "\" " +
//                    "sockets=\"" + socketCount + "\" " +
                            "/>\n\n");
                }
                // output channel for EB module
                out.append("         <outchannel id=\"" + md.getId() + "\" " +
                        "name=\"" + cmp.getName() + "_ER" + "\" " +
                        "transp=\"Fifo\" " +
                        "/>\n\n");
                out.append("     </EbModule>\n\n");

                // ER module =============================================
                if (cmp.isStreaming()) {
                    if (isEndianLittle) {
                        out.append("     <ErModule class=\"" + md.getModuleClass(ACodaType.ER.name()) + "\" " +
                                "streaming=\"" + "on" + "\" " +
                                "id=\"" + md.getId() + "\" " +
                                "timeStats=\"off\" " +
                                "endian=\"" + "little" + "\"" +
                                "> \n\n");
                    } else {
                        out.append("     <ErModule class=\"" + md.getModuleClass(ACodaType.ER.name()) + "\" " +
                                "streaming=\"" + "on" + "\" " +
                                "id=\"" + md.getId() + "\" " +
                                "timeStats=\"off\" " +
                                "> \n\n");
                    }
                } else {
                    if (isEndianLittle) {
                        out.append("     <ErModule class=\"" + md.getModuleClass(ACodaType.ER.name()) + "\" " +
                                "id=\"" + md.getId() + "\" " +
                                "timeStats=\"off\" " +
                                "endian=\"" + "little" + "\"" +
                                "> \n\n");
                    } else {
                        out.append("     <ErModule class=\"" + md.getModuleClass(ACodaType.ER.name()) + "\" " +
                                "id=\"" + md.getId() + "\" " +
                                "timeStats=\"off\" " +
                                "> \n\n");
                    }
                }
                // input channel for ER module
                out.append("         <inchannel id=\"" + md.getId() + "\" " +
                        "name=\"" + cmp.getName() + "_ER" + "\" " +
                        "transp=\"Fifo\" " +
                        "/>\n\n");
                // input channel for ET

                for (JCGChannel ch : ec.getiChannels().values()) {
                    if (ch.getTransport() != null && ch.getTransport().getTransClass().equals("EmuSocket+Et")) {
                        out.append("         <inchannel id=\"" + (ch.getId() + 1) + "\" " +
                                "name=\"et_input\" " +
                                "transp=\"" + ch.getTransport().getName() +
                                "_async" +
                                "\" " +
                                "chunk=\"" + ch.getTransport().getInputEtChunkSize() + "\" " +
                                "stationName=\"inputStation\"" +
                                " ignoreErrors=\"true\"" +
                                "/>\n\n");
                        break;
                    }
                }
            }
        }
        return out.toString();
    }

    private String writeModule(JCGComponent cmp) {
        StringBuilder out = new StringBuilder();
        JCGModule md;
        boolean isEndianLittle = false;

        String endian = "big";
        md = cmp.getModule();
        if (md != null) {

            for (JCGChannel ch : md.getChnnels()) {
                if (ch.getEndian().equals("little")) {
                    isEndianLittle = true;
                    break;
                }
            }
            if (isEndianLittle) endian = "little";

            // here we assume that all modules share the same source and USR source
            out.append("   <modules>\n\n");
            // ------ ER --------
            if (cmp.getType().equals(ACodaType.ER.name())) {
                if (cmp.isStreaming()) {
                    out.append("     <ErModule class=\"" + md.getModuleClass(ACodaType.ER.name()) + "\" " +
                            "streaming=\"" + "on" + "\" " +
                            "id=\"" + md.getId() + "\" " +
                            "timeStats=\"off\" " +
                            "endian=\"" + endian + "\"" +
                            "> \n\n");
                } else {
                    out.append("     <ErModule class=\"" + md.getModuleClass(ACodaType.ER.name()) + "\" " +
                            "id=\"" + md.getId() + "\" " +
                            "timeStats=\"off\" " +
                            "endian=\"" + endian + "\"" +
                            "> \n\n");
                }
                // ------ GT --------
            } else if (cmp.getType().equals(ACodaType.GT.name())) {
                if (cmp.isStreaming()) {
                    out.append("     <GTriggerModule class=\"" + md.getModuleClass(ACodaType.GT.name()) + "\" " +
                            "streaming=\"" + "on" + "\" " +
                            "id=\"" + md.getId() + "\" " +
                            "timeStats=\"off\" " +
                            "> \n\n");
                } else {
                    out.append("     <GTriggerModule class=\"" + md.getModuleClass(ACodaType.GT.name()) + "\" " +
                            "id=\"" + md.getId() + "\" " +
                            "timeStats=\"off\" " +
                            "> \n\n");
                }

                // ------ FPGA --------
            } else if (cmp.getType().equals(ACodaType.FPGA.name())) {
                if (cmp.isStreaming()) {
                    out.append("     <FPGATriggerModule class=\"" + md.getModuleClass(ACodaType.FPGA.name()) + "\" " +
                            "streaming=\"" + "on" + "\" " +
                            "id=\"" + md.getId() + "\" " +
                            "timeStats=\"off\" " +
                            "> \n\n");
                } else {
                    out.append("     <FPGATriggerModule class=\"" + md.getModuleClass(ACodaType.FPGA.name()) + "\" " +
                            "id=\"" + md.getId() + "\" " +
                            "timeStats=\"off\" " +
                            "> \n\n");
                }

                // ------ USR --------
            } else if (cmp.getType().equals(ACodaType.USR.name())) {
                if (cmp.isStreaming()) {
                    out.append("     <UsrModule class=\"" + cmp.getUserConfig() + "\" " +
                            "streaming=\"" + "on" + "\" " +
                            "id=\"" + md.getId() + "\" " +
                            "> \n\n");
                } else {
                    out.append("     <UsrModule class=\"" + cmp.getUserConfig() + "\" " +
                            "id=\"" + md.getId() + "\" " +
                            "> \n\n");
                }

                // ------ TS --------
            } else if (cmp.getType().equals(ACodaType.TS.name())) {
                List<String> r_list = new ArrayList<>();
                if (cmp.isStreaming()) {
                    for (JCGComponent c : components) {
                        if (c.getType().equals(ACodaType.ROC.name())) {
                            r_list.add(c.getName());
                        }
                    }
                    out.append("     <TsModule class=\"" + md.getModuleClass(ACodaType.TS.name()) + "\" " +
                            "streaming=\"" + "on" + "\" ");
                    int i = 0;
                    for (String s : r_list) {
                        i = i + 1;
                        out.append("r" + i + "=\"" + s + "\" ");
                    }
                } else {
                    for (JCGComponent c : components) {
                        if (c.getType().equals(ACodaType.ROC.name())) {
                            r_list.add(c.getName());
                        }
                    }
                    out.append("     <TsModule class=\"" + md.getModuleClass(ACodaType.TS.name()) + "\" ");
                    int i = 0;
                    for (String s : r_list) {
                        i = i + 1;
                        out.append("r" + i + "=\"" + s + "\" ");
                    }
                }
                out.append("> \n\n");

                // ------ ROC --------
            } else if (cmp.getType().equals(ACodaType.ROC.name())) {
                if (cmp.isStreaming()) {
                    out.append("     <RocModule class=\"" + md.getModuleClass(ACodaType.ROC.name()) + "\" " +
                            "streaming=\"" + "on" + "\" " +
                            "id=\"" + md.getId() + "\" " +
                            "timeStats=\"off\" " +
                            "endian=\"" + endian + "\"" +
                            "> \n\n");
                } else {
                    out.append("     <RocModule class=\"" + md.getModuleClass(ACodaType.ROC.name()) + "\" " +
                            "id=\"" + md.getId() + "\" " +
                            "timeStats=\"off\" " +
                            "endian=\"" + endian + "\"" +
                            "> \n\n");
                }

                // -------- All the rest of types (DC, PEB, SEB, EB) -----
            } else {
                if (cmp.isStreaming()) {
                    out.append("     <EbModule class=\"" + "Aggregator" + "\" " +
                            "streaming=\"" + "on" + "\" " +
                            "id=\"" + md.getId() + "\" " +
                            "threads=\"" + md.getThreads() + "\" " +
                            "timeStats=\"off\" " +
                            "runData=\"" + md.isRunData() + "\" " +
                            "tsCheck=\"" + md.isTsCheck() + "\" " +
                            "tsSlop=\"" + md.getTsSlop() + "\" " +
                            "sparsify=\"" + md.isSparsify() + "\" " +
                            "endian=\"" + endian + "\"" +
                            "> \n\n");
                } else {
                    out.append("     <EbModule class=\"" + md.getModuleClass(ACodaType.PEB.name()) + "\" " +
                            "id=\"" + md.getId() + "\" " +
                            "threads=\"" + md.getThreads() + "\" " +
                            "timeStats=\"off\" " +
                            "runData=\"" + md.isRunData() + "\" " +
                            "tsCheck=\"" + md.isTsCheck() + "\" " +
                            "tsSlop=\"" + md.getTsSlop() + "\" " +
                            "sparsify=\"" + md.isSparsify() + "\" " +
                            "endian=\"" + endian + "\"" +
                            "> \n\n");
                }
            }
        }
        return out.toString();
    }

    private String writeOutChannels(JCGChannel ch, boolean isFat, int id) {
        StringBuilder out = new StringBuilder();
        // check if the transport is file
        if (ch.getTransport() != null && ch.getTransport().getTransClass().equals("File")) {
            out.append("         <outchannel id=\"" + id + "\" " +
                    "name=\"" + ch.getName() + "\" " +
                    "transp=\"" + ch.getTransport().getName() + "\" " +
                    "fileName=\"" + ch.getTransport().getFileName() + "\" " +
                    "split=\"" + ch.getTransport().getFileSplit() + "\" " +
//                    "internalBuf=\"" + ch.getTransport().getFileInternalBuffer() + "\" " +
                    "/>\n\n");

        } else if (ch.getTransport() != null && ch.getTransport().getTransClass().equals("Et")) {
            out.append("         <outchannel id=\"" + id + "\" " +
                    "name=\"" + ch.getName() + "\" " +
                    "transp=\"" + ch.getTransport().getName() + "\" " +
                    "group=\"" + ch.getGroup() + "\" " +
                    "chunk=\"" + ch.getTransport().getEtChunkSize() + "\" " +
                    "single=\"" + ch.getTransport().getSingle() + "\" " +
                    "/>\n\n");

        } else if (ch.getTransport() != null && ch.getTransport().getTransClass().equals("EmuSocket")) {
            int socketCount = 1;
//            if (ch.getTransport().isEmuFatPipe()) socketCount = 2;
            if (isFat) socketCount = 2;
            if (ch.getTransport().getEmuSubNet().equals("undefined") || ch.getTransport().getEmuSubNet().equals("")) {
                out.append("         <outchannel id=\"" + id + "\" " +
                        "name=\"" + ch.getName() + "\" " +
                        "transp=\"" + ch.getTransport().getName() + "\" " +
                        "timeout=\"" + ch.getTransport().getEmuWait() + "\" " +
                        "port=\"" + ch.getTransport().getEmuDirectPort() + "\" " +
                        "maxBuf=\"" + ch.getTransport().getEmuMaxBuffer() + "\" " +
                        "sockets=\"" + socketCount + "\" " +
                        "/>\n\n");
            } else {
                out.append("         <outchannel id=\"" + id + "\" " +
                        "name=\"" + ch.getName() + "\" " +
                        "transp=\"" + ch.getTransport().getName() + "\" " +
                        "timeout=\"" + ch.getTransport().getEmuWait() + "\" " +
                        "port=\"" + ch.getTransport().getEmuDirectPort() + "\" " +
                        "subnet=\"" + ch.getTransport().getEmuSubNet() + "\" " +
                        "maxBuf=\"" + ch.getTransport().getEmuMaxBuffer() + "\" " +
                        "sockets=\"" + socketCount + "\" " +
                        "/>\n\n");
            }
        } else if (ch.getTransport() != null && ch.getTransport().getTransClass().equals("EmuSocket+Et")) {
            int socketCount = 1;
//            if (ch.getTransport().isEmuFatPipe()) socketCount = 2;
            if (isFat) socketCount = 2;
            if (ch.getTransport().getEmuSubNet().equals("undefined") || ch.getTransport().getEmuSubNet().equals("")) {
                out.append("         <outchannel id=\"" + id + "\" " +
                        "name=\"" + ch.getName() + "\" " +
                        "transp=\"" + ch.getTransport().getName() + "\" " +
                        "timeout=\"" + ch.getTransport().getEmuWait() + "\" " +
                        "port=\"" + ch.getTransport().getEmuDirectPort() + "\" " +
                        "maxBuf=\"" + ch.getTransport().getEmuMaxBuffer() + "\" " +
                        "sockets=\"" + socketCount + "\" " +
                        "/>\n\n");
            } else {
                out.append("         <outchannel id=\"" + id + "\" " +
                        "name=\"" + ch.getName() + "\" " +
                        "transp=\"" + ch.getTransport().getName() + "\" " +
                        "timeout=\"" + ch.getTransport().getEmuWait() + "\" " +
                        "port=\"" + ch.getTransport().getEmuDirectPort() + "\" " +
                        "subnet=\"" + ch.getTransport().getEmuSubNet() + "\" " +
                        "maxBuf=\"" + ch.getTransport().getEmuMaxBuffer() + "\" " +
                        "sockets=\"" + socketCount + "\" " +
                        "/>\n\n");
            }
        } else if (ch.getTransport() != null && ch.getTransport().getTransClass().equals("TcpStream")) {
            out.append("         <outchannel id=\"" + id + "\" " +
                    "name=\"" + ch.getName() + "\" " +
                    "transp=\"" + ch.getTransport().getName() + "\" " +
                    "timeout=\"" + ch.getTransport().getTcpStreamWait() + "\" " +
                    "port=\"" + ch.getTransport().getTcpStreamDirectPort() + "\" " +
                    "maxBuf=\"" + ch.getTransport().getTcpStreamMaxBuffer() + "\" " +
                    "/>\n\n");

        } else if (ch.getTransport() != null && ch.getTransport().getTransClass().equals("UdpStream")) {
            out.append("         <outchannel id=\"" + id + "\" " +
                    "name=\"" + ch.getName() + "\" " +
                    "transp=\"" + ch.getTransport().getName() + "\" " +
                    "port=\"" + ch.getTransport().getUdpPort() + "\" " +
                    "bufSize=\"" + ch.getTransport().getUdpBufferSize() + "\" " +
                    "host=\"" + ch.getTransport().getUdpHost() + "\" " +
                    "useLoadBalancer=\"" + ch.getTransport().isLB() + "\" " +
                    "useErsapReHeader=\"" + ch.getTransport().isErsap() + "\" " +
                    "/>\n\n");

        }
        return out.toString();
    }

    private String writeInChannels(String cName, String cType, JCGChannel ch) {
        StringBuilder out = new StringBuilder();
        // check if the transport is file
        if (ch.getTransport() != null && ch.getTransport().getTransClass().equals("File")) {
            out.append("         <inchannel id=\"" + ch.getId() + "\" " +
                    "name=\"" + ch.getName() + "\" " +
                    "transp=\"" + ch.getTransport().getName() + "\" " +
                    "fileName=\"" + ch.getTransport().getFileName() + "\" " +
                    "split=\"" + ch.getTransport().getFileSplit() + "\" " +
//                    "internalBuf=\"" + ch.getTransport().getFileInternalBuffer() + "\" " +
                    "/>\n\n");

        } else if (ch.getTransport() != null && ch.getTransport().getTransClass().equals("Et")) {
            if (cType != null && !(cType.equals(ACodaType.ER.name()))) {

                out.append("         <inchannel id=\"" + ch.getId() + "\" " +
                        "name=\"" + ch.getName() + "\" " +
                        "transp=\"" + ch.getTransport().getName() + "\" " +
                        "chunk=\"" + ch.getTransport().getInputEtChunkSize() + "\" " +
                        "idFilter=\"" + ch.getIdFilter() + "\" " +
                        "/>\n\n");
            } else {
                out.append("         <inchannel id=\"" + ch.getId() + "\" " +
                        "name=\"" + ch.getName() + "\" " +
                        "transp=\"" + ch.getTransport().getName() + "\" " +
                        "chunk=\"" + ch.getTransport().getInputEtChunkSize() + "\" " +
                        "/>\n\n");
            }
        } else if (ch.getTransport() != null && ch.getTransport().getTransClass().equals("EmuSocket")) {

            out.append("         <inchannel id=\"" + ch.getId() + "\" " +
                    "name=\"" + ch.getName() + "\" " +
                    "transp=\"" + ch.getTransport().getName() + "\" " +
//                    "sockets=\"" + socketCount + "\" " +
                    "/>\n\n");
        } else if (ch.getTransport() != null && ch.getTransport().getTransClass().equals("EmuSocket+Et")) {
            out.append("         <inchannel id=\"" + ch.getId() + "\" " +
                    "name=\"" + ch.getName() + "\" " +
                    "transp=\"" + ch.getTransport().getName() + "\" " +
//                    "sockets=\"" + socketCount + "\" " +
                    "/>\n\n");
            if (cType != null && cType.equals(ACodaType.ER.name())) {
                out.append("         <inchannel id=\"" + (ch.getId() + 1) + "\" " +
                        "name=\"et_input\" " +
                        "transp=\"" + ch.getTransport().getName() +
                        "_async" +
                        "\" " +
                        "chunk=\"" + ch.getTransport().getInputEtChunkSize() + "\" " +
                        "stationName=\"inputStation\"" +
                        " ignoreErrors=\"true\"" +
                        "/>\n\n");

            }
        } else if (ch.getTransport() != null && ch.getTransport().getTransClass().equals("TcpStream")) {
            out.append("         <inchannel id=\"" + ch.getId() + "\" " +
                    "name=\"" + ch.getName() + "\" " +
                    "transp=\"" + ch.getTransport().getName() + "\" " +
                    "streams=\"" + ch.getTransport().getEmuTcpStreams() + "\" " +
                    "/>\n\n");

        } else if (ch.getTransport() != null && ch.getTransport().getTransClass().equals("UdpStream")) {
            out.append("         <inchannel id=\"" + ch.getId() + "\" " +
                    "name=\"" + ch.getName() + "\" " +
                    "transp=\"" + ch.getTransport().getName() + "\" " +
                    "port=\"" + ch.getTransport().getUdpPort() + "\" " +
                    "bufSize=\"" + ch.getTransport().getUdpBufferSize() + "\" " +
                    "streams=\"" + ch.getTransport().getUdpStreams() + "\" " +
                    "useErsapReHeader=\"" + ch.getTransport().isErsap() + "\" " +
                    "/>\n\n");

        }
        return out.toString();
    }

    private boolean createRocConfigFile(String cName) {
        boolean b = true;
        boolean isEndianLittle = false;

        String fileName = filePath + cName + ".dat";
        JCGComponent cmp = null;
        for (JCGComponent c : components) {
            if (c.getName().equals(cName)) {
                cmp = c;
                break;
            }
        }
        if (cmp == null) return false;

        JCGModule md = cmp.getModule();
        if (md != null) {

            for (JCGChannel ch : md.getChnnels()) {
                if (ch.getEndian().equals("little")) {
                    isEndianLittle = true;
                    break;
                }
            }
        }

        try {

            BufferedWriter out = new BufferedWriter(new FileWriter(fileName));

            if (cmp.getType().equals(ACodaType.ROC.name()) ||
                    cmp.getType().equals(ACodaType.TS.name()) ||
                    cmp.getType().equals(ACodaType.FPGA.name()) ||
                    cmp.getType().equals(ACodaType.GT.name())
            ) {

                int group = 0;

                out.write("name                  = " + cmp.getName() + "\n");
                out.write("type                  = " + cmp.getType() + "\n");
                out.write("priority              = " + cmp.getPriority() + "\n");
                out.write("code                  = " + cmp.createCode() + "\n");
                out.write("isMaster              = " + cmp.isMaster() + "\n");
                boolean breakFlag = false;

                if (cmp.getLinks().isEmpty()) {
                    out.write("output                = None \n");

                } else {
                    for (JCGLink l : cmp.getLinks()) {

                        if (l.getDestinationComponentName() != null &&
                                _compMap.get(l.getDestinationComponentName()).getTransports() != null) {

                            for (JCGChannel ch : _compDat.get(l.getSourceComponentName()).getoChannels().values()) {
                                group = ch.getGroup();
                            }

                            for (JCGTransport tt : _compMap.get(l.getDestinationComponentName()).getTransports()) {
                                if (tt.getName().equals(l.getDestinationTransportName())) {


                                    switch (tt.getTransClass()) {
                                        case "Et":
                                            out.write("output                = " + tt.getTransClass() + "\n");
                                            out.write("etName                = " + tt.getEtName() + "\n");
                                            if (tt.getEtMethodCon().equals("direct")) {
                                                out.write("etHost                = " + tt.getEtHostName() + "\n");
                                                out.write("etPort                = " + tt.getEtTcpPort() + "\n");
                                                out.write("etGroup               = " + group + "\n");
                                            } else if (tt.getEtMethodCon().equals("mcast")) {
                                                out.write("etHost                = " + tt.getmAddress() + "\n");
                                                out.write("etPort                = " + tt.getEtUdpPort() + "\n");
                                                out.write("etGroup               = " + group + "\n");
                                            }

                                            break;
                                        case "EmuSocket":
                                            out.write("output                = " + tt.getTransClass() + "\n");
                                            out.write("emuName               = " + l.getDestinationComponentName() + "\n");
                                            out.write("emuPort               = " + tt.getEmuDirectPort() + "\n");
                                            if (cmp.getType().equals(ACodaType.FPGA.name())) {
                                                out.write("fpgaNet               = " + tt.getFpgaLinkIp() + "\n");
                                                out.write("emuNet                = NA" + "\n");

                                            } else {
                                                out.write("fpgaNet               = NA" + "\n");
                                                out.write("emuNet                = " + tt.getEmuSubNet() + "\n");
                                            }
                                            out.write("emuMaxBufferSize      = " + tt.getEmuMaxBuffer() + "\n");
                                            out.write("emuTimeOut            = " + tt.getEmuWait() + "\n");

                                            break;

                                        case "EmuSocket+Et":
                                            out.write("output                = EmuSocket\n");
                                            out.write("emuName               = " + l.getDestinationComponentName() + "\n");
                                            out.write("emuPort               = " + tt.getEmuDirectPort() + "\n");
                                            if (cmp.getType().equals(ACodaType.FPGA.name())) {
                                                out.write("fpgaNet                = " + tt.getFpgaLinkIp() + "\n");
                                            } else {
                                                out.write("emuNet                = " + tt.getEmuSubNet() + "\n");
                                            }
                                            out.write("emuMaxBufferSize      = " + tt.getEmuMaxBuffer() + "\n");
                                            out.write("emuTimeOut            = " + tt.getEmuWait() + "\n");

                                            break;
                                        case "UdpStream":
                                            out.write("output                = " + tt.getTransClass() + "\n");
                                            out.write("emuName               = " + l.getDestinationComponentName() + "\n");
                                            out.write("emuHost               = " + tt.getUdpHost() + "\n");
                                            out.write("emuPort               = " + tt.getUdpPort() + "\n");
                                            if (cmp.getType().equals(ACodaType.FPGA.name())) {
                                                out.write("fpgaNet               = " + tt.getUdpFpgaLinkIp() + "\n");
                                                out.write("emuNet                = NA" + "\n");
                                            }
                                            out.write("emuMaxBufferSize      = " + tt.getUdpBufferSize() + "\n");
                                            out.write("emuLinks              = " + tt.getUdpStreams() + "\n");

                                            break;
                                        case "TcpStream":
                                            out.write("output                = " + tt.getTransClass() + "\n");
                                            out.write("emuName               = " + l.getDestinationComponentName() + "\n");
                                            out.write("emuPort               = " + tt.getTcpStreamDirectPort() + "\n");
                                            if (cmp.getType().equals(ACodaType.FPGA.name())) {
                                                out.write("fpgaNet               = " + tt.getTcpStreamFpgaLinkIp() + "\n");
                                                out.write("emuNet                = NA" + "\n");
                                            } else {
                                                out.write("fpgaNet               = NA" + "\n");
                                                out.write("emuNet                = " + tt.getTcpStreamSubNet() + "\n");
                                            }
                                            out.write("emuMaxBufferSize      = " + tt.getTcpStreamMaxBuffer() + "\n");
                                            out.write("emuLinks              = " + tt.getEmuTcpStreams() + "\n");

                                            break;
                                        case "File":
                                            out.write("output                = " + tt.getTransClass() + "\n");
                                            out.write("dataFile              = " + tt.getFileName() + "\n");
                                            out.write("fileType              = " + tt.getFileType() + "\n");

                                            break;
                                        case "None":
                                        case "Debug":
                                            out.write("output                = " + tt.getTransClass() + "\n");
                                            break;
                                    }
                                    breakFlag = true;
                                    break;
                                }
                            }
                        }
                        if (breakFlag) break;
                    }
                }

            } else if (cmp.getType().equals(ACodaType.USR.name())) {

                out.write("name                  = " + cmp.getName() + "\n");
                out.write("type                  = " + cmp.getType() + "\n");
                out.write("priority              = " + cmp.getPriority() + "\n");

                for (ExternalConfig ec : _compDat.values()) {
                    if (ec.getName().equals(cmp.getName())) {
                        ArrayList<String> tpNames = new ArrayList<>();
                        for (JCGTransport tr : ec.getTransports()) {
                            // avoid writing the same transport twice.
                            String tName = tr.getName();
                            String io;
                            if (tName.startsWith(ec.getName())) {
                                io = "input ";
                            } else {
                                io = "output";
                            }
                            if (!tpNames.contains(tName)) {

                                switch (tr.getTransClass()) {
                                    case "Et":
                                        out.write(io + " transportClass = " + tr.getTransClass() + "\n");
                                        out.write("etName                = " + tr.getEtName() + "\n");
                                        out.write("etHost                = " + tr.getmAddress() + "\n");
                                        out.write("etPort                = " + tr.getEtTcpPort() + "\n");
                                        out.write("etUdpPort             = " + tr.getEtUdpPort() + "\n");
                                        out.write("eventNum              = " + tr.getEtEventNum() + "\n");
                                        out.write("eventSize             = " + tr.getEtEventSize() + "\n");
                                        out.write("method                = " + tr.getEtMethodCon() + "\n");
                                        out.write("subnet                = " + tr.getEtSubNet() + "\n");
                                        out.write("wait                  = " + tr.getEtWait() + "\n");
                                        break;
                                    case "EmuSocket":
                                        out.write(io + " transportClass = " + tr.getTransClass() + "\n");
                                        out.write("emuPort               = " + tr.getEmuDirectPort() + "\n");
                                        out.write("emuNet                = " + tr.getEmuSubNet() + "\n");
                                        out.write("emuMaxBufferSize      = " + tr.getEmuMaxBuffer() + "\n");
                                        out.write("emuTimeOut            = " + tr.getEmuWait() + "\n");
                                        break;
                                    case "EmuSocket+Et":
                                        out.write("output                = EmuSocket\n");
                                        out.write("emuPort               = " + tr.getEmuDirectPort() + "\n");
                                        out.write("emuNet                = " + tr.getEmuSubNet() + "\n");
                                        out.write("emuMaxBufferSize      = " + tr.getEmuMaxBuffer() + "\n");
                                        out.write("emuTimeOut            = " + tr.getEmuWait() + "\n");
                                        break;
                                    case "File":
                                        out.write(io + " transportClass = " + tr.getTransClass() + "\n");
                                        out.write("dataFile              = " + tr.getFileName() + "\n");
                                        out.write("fileType              = " + tr.getFileType() + "\n");
                                        out.write("splitBytes            = " + tr.getFileSplit() + "\n");
                                        ec.getiChannels().size();

                                        break;
                                }
                                tpNames.add(tName);
                            }
                        }
                        if (isEndianLittle) {
                            out.write("endian                = little \n");
                        }
                    }
                }
            }

            out.close();

        } catch (IOException e) {
            b = false;
            e.printStackTrace();
        }
        return b;
    }

//    private void serializeDumpComponent(String cmpName){
//        JAXBContext context;
//        JCGComponent cmp = null;
//        for(JCGComponent c:components){
//            if(c.getName().equals(cmpName)){
//                cmp = c;
//                break;
//            }
//        }
//        try {
//            context = JAXBContext.newInstance(JCGComponent.class);
//            Marshaller m = context.createMarshaller();
//            m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
////        m.marshal(cmp, System.out);
//            m.marshal(cmp, new File(filePath+"."+cmpName+"_p.xml"));
//        } catch (JAXBException e) {
//            e.printStackTrace();
//        }
//    }

    /**
     * Returns component input links
     *
     */
    private ArrayList<JCGLink> getInputLinks(JCGComponent cmp) {
        ArrayList<JCGLink> lo = new ArrayList<JCGLink>();
        for (JCGLink l : cmp.getLinks()) {
            if (l.getDestinationComponentName().equals((cmp.getName()))) {
                lo.add(l);
            }
        }
        return lo;
    }

    private ArrayList<JCGLink> getOutputLinks(JCGComponent cmp) {
        ArrayList<JCGLink> lo = new ArrayList<>();
        for (JCGLink l : cmp.getLinks()) {
            if (l.getSourceComponentName().equals((cmp.getName()))) {
                lo.add(l);
            }
        }
        return lo;
    }

    private JCGTransport getDestinationTransport(JCGLink l) {

        StringTokenizer st = new StringTokenizer(l.getName(), "_");
        st.nextToken();
        String dName = st.nextToken();
//        for(JCGTransport t:DrawingCanvas.getComp(l.getDestinationComponentName()).getTrnsports()){
        for (JCGTransport t : _compMap.get(l.getDestinationComponentName()).getTransports()) {
            if (t.getName().startsWith(dName)) {
                return t;
            }
        }
        return null;
    }

    private JCGTransport getSourceTransport(JCGLink l) {
        StringTokenizer st = new StringTokenizer(l.getName(), "_");
        String dName = st.nextToken();
//        for(JCGTransport t:DrawingCanvas.getComp(l.getSourceComponentName()).getTrnsports()){
        for (JCGTransport t : _compMap.get(l.getSourceComponentName()).getTransports()) {
            if (t.getName().startsWith(dName)) {
                return t;
            }
        }
        return null;
    }

    private JCGChannel getDestinationChannel(JCGLink l) {
        JCGChannel ch = null;
//        String mdName = DrawingCanvas.getComp(l.getDestinationComponentName()).getModule().getName();
        String mdName = _compMap.get(l.getDestinationComponentName()).getModule().getName();
        if (mdName.equals(l.getDestinationModuleName())) {

            // first get source component object ( component that this destination is linked to)
//            JCGComponent sc = DrawingCanvas.getComp(l.getSourceComponentName());
            JCGComponent sc = _compMap.get(l.getSourceComponentName());
            // get selected transport defined for this (destination) component
            JCGTransport tr = getDestinationTransport(l);
            // check if the transport is file
            if (tr != null && sc != null) {
                ch = new JCGChannel();
                ch.setId(sc.getId());
                ch.setName(sc.getName());
                ch.setTransport(tr);
                ch.setIdFilter(l.isDestinationIdFilter());
                ch.setChunk(tr.getEtChunkSize());
            }
        }
        return ch;
    }

    private JCGChannel getSourceChannel(JCGLink l) {
        JCGChannel ch = null;
        String mdName = _compMap.get(l.getDestinationComponentName()).getModule().getName();
        if (mdName.equals(l.getDestinationModuleName())) {

            // first get source component object ( component that this destination is linked to)
//            JCGComponent dc = DrawingCanvas.getComp(l.getDestinationComponentName());
            JCGComponent dc = _compMap.get(l.getDestinationComponentName());
            // get selected transport defined for this (destination) component
            JCGTransport tr = getDestinationTransport(l);
            // check if the transport is file
            if (tr != null && dc != null) {
                ch = new JCGChannel();
                ch.setId(dc.getId());
                ch.setName(dc.getName());
                ch.setTransport(tr);
                ch.setIdFilter(l.isDestinationIdFilter());
            }
        }
        return ch;
    }


    public ArrayList<String> getCSVElements(String csv) {
        ArrayList<String> o = new ArrayList<String>();
        StringTokenizer st = new StringTokenizer(csv, " ,");
        while (st.hasMoreTokens()) {
            o.add(st.nextToken().trim());
        }
        return o;
    }

    public void createFCSConfigFile(JCGComponent component) {
        String iEtName = "undefined";
        String iEtHost = "undefined";
        int iEtPort = 0;
        String oEtName = "undefined";
        String oEtHost = "undefined";
        int oEtPort = 0;

        String fileFullName = filePath + component.getName() + "_fc.xml";

        // extract both ET information from the output link
        ArrayList<JCGLink> ol = getOutputLinks(component);
        if (!ol.isEmpty()) {
            for (JCGLink l : ol) {
                for (JCGTransport tt : _compMap.get(l.getSourceComponentName()).getTransports()) {
                    if (tt.getName().equals(l.getSourceTransportName())) {

                        if (tt.getTransClass().equals("Et")) {
                            iEtName = tt.getEtName();

                            if (tt.getEtMethodCon().equals("direct")) {
                                iEtHost = tt.getEtHostName();
                                iEtPort = tt.getEtTcpPort();
                            } else if (tt.getEtMethodCon().equals("mcast")) {
                                iEtHost = tt.getmAddress();
                                iEtPort = tt.getEtUdpPort();
                            }
                        }
                        break;
                    }
                }

                for (JCGTransport tt : _compMap.get(l.getDestinationComponentName()).getTransports()) {
                    if (tt.getName().equals(l.getDestinationTransportName())) {

                        if (tt.getTransClass().equals("Et")) {
                            oEtName = tt.getEtName();

                            if (tt.getEtMethodCon().equals("direct")) {
                                oEtHost = tt.getEtHostName();
                                oEtPort = tt.getEtTcpPort();
                            } else if (tt.getEtMethodCon().equals("mcast")) {
                                oEtHost = tt.getmAddress();
                                oEtPort = tt.getEtUdpPort();
                            }
                        }
                        break;
                    }
                }
            }
        }

        try {
            BufferedWriter out = new BufferedWriter(new FileWriter(fileFullName));
            out.write("<?xml version=\"1.0\"?>\n");
            out.write("<component name=\"" + component.getName() + "\">\n");

            out.write("   <command>\n");
            out.write("      " + component.getCommand() + "\n");
            out.write("   </command>\n");

            out.write("   <inputEt>\n");
            out.write("      <name>" + iEtName + "</name>\n");
            out.write("      <host>" + iEtHost + "</host>\n");
            out.write("      <port>" + iEtPort + "</port>\n");
            out.write("   </inputEt>\n");

            out.write("   <outputEt>\n");
            out.write("      <name>" + oEtName + "</name>\n");
            out.write("      <host>" + oEtHost + "</host>\n");
            out.write("      <port>" + oEtPort + "</port>\n");
            out.write("   </outputEt>\n");

//            out.write("   <nodes>\n");
//            for(String nome:getCSVElements(component.getNodeList())){
//                out.write("     <node>" + nome + "</node>\n");
//            }
//            out.write("   </nodes>\n");

            out.write("</component>\n");
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private class ExternalConfig {
        private String name;
        private String type;
        private boolean codaVersion2 = false;
        private HashSet<JCGTransport>
                transports = new HashSet<JCGTransport>();
        private HashMap<String, JCGChannel>
                iChannels = new HashMap<String, JCGChannel>();
        private HashMap<String, JCGChannel>
                oChannels = new HashMap<String, JCGChannel>();

        private boolean isFat;

        public boolean isFat() {
            return isFat;
        }

        public void setFat(boolean fat) {
            isFat = fat;
        }

        public boolean isStreaming() {
            return codaVersion2;
        }

        public void setStreaming(boolean s) {
            codaVersion2 = s;
        }

        public String getName() {
            return name;
        }

        public void setName(String cName) {
            this.name = cName;
        }

        public String getType() {
            return type;
        }

        public void setType(String cType) {
            this.type = cType;
        }

        public void addTransport(JCGTransport t) {
            transports.add(t);
        }

        public void removeTransport(JCGTransport t) {
            transports.remove(t);
        }

        public void addInputChannel(JCGChannel c) {
            iChannels.put(c.getName(), c);
        }

        public void removeInputChannel(JCGChannel c) {
            iChannels.remove(c.getName());
        }

        public void addOutputChannel(JCGChannel c) {
            oChannels.put(c.getName(), c);
        }

        public void removeOutputChannel(JCGChannel c) {
            oChannels.remove(c.getName());
        }

        public HashSet<JCGTransport> getTransports() {
            return transports;
        }

        public HashMap<String, JCGChannel> getiChannels() {
            return iChannels;
        }

        public HashMap<String, JCGChannel> getoChannels() {
            return oChannels;
        }
    }


}
