package org.jlab.epsci.dpux.dpol.concepts;

import java.util.List;

public class DPComposition {
    private List<DPComponent> components;
    private List<DPTransport> transports;
    private List<DPProcess> processes;
    private List<DPLink> links;

    public DPComposition(List<DPComponent> components, List<DPTransport> transports, List<DPProcess> processes, List<DPLink> links) {
        this.components = components;
        this.transports = transports;
        this.processes = processes;
        this.links = links;
    }

    public List<DPComponent> getComponents() {
        return components;
    }

    public void setComponents(List<DPComponent> components) {
        this.components = components;
    }

    public List<DPTransport> getTransports() {
        return transports;
    }

    public void setTransports(List<DPTransport> transports) {
        this.transports = transports;
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
}
