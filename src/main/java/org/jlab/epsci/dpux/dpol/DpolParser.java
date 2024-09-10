package org.jlab.epsci.dpux.dpol;

import org.apache.jena.query.*;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.util.FileManager;
import org.jlab.epsci.dpux.dpol.concepts.*;

import java.util.ArrayList;
import java.util.List;

public class DpolParser {

    private static final String ONTOLOGY_PATH = "path/to/dpol.owl";  // Update with actual path
    private static final String NAMESPACE = "http://www.jlab.org/ontology/dpol#";

    public static DPComposition parseOntology() {
        // Load the ontology using Apache Jena
        Model model = FileManager.get().loadModel(ONTOLOGY_PATH);

        // Create lists to store parsed objects
        List<DPComponent> components = new ArrayList<>();
        List<DPTransport> transports = new ArrayList<>();
        List<DPProcess> processes = new ArrayList<>();
        List<DPLink> links = new ArrayList<>();

        // Query and parse Components
        String componentQuery =
                "PREFIX dpol: <" + NAMESPACE + "> " +
                        "SELECT ?component WHERE { ?composition dpol:hasComponent ?component . }";
        ResultSet componentResults = executeQuery(model, componentQuery);
        while (componentResults.hasNext()) {
            QuerySolution sol = componentResults.nextSolution();
            Resource componentRes = sol.getResource("component");
            components.add(new DPComponent(componentRes.getLocalName()));
        }

        // Query and parse Transports
        String transportQuery =
                "PREFIX dpol: <" + NAMESPACE + "> " +
                        "SELECT ?transport WHERE { ?composition dpol:hasTransport ?transport . }";
        ResultSet transportResults = executeQuery(model, transportQuery);
        while (transportResults.hasNext()) {
            QuerySolution sol = transportResults.nextSolution();
            Resource transportRes = sol.getResource("transport");
            transports.add(new DPTransport(transportRes.getLocalName()));
        }

        // Query and parse Processes
        String processQuery =
                "PREFIX dpol: <" + NAMESPACE + "> " +
                        "SELECT ?process WHERE { ?composition dpol:hasProcess ?process . }";
        ResultSet processResults = executeQuery(model, processQuery);
        while (processResults.hasNext()) {
            QuerySolution sol = processResults.nextSolution();
            Resource processRes = sol.getResource("process");
            processes.add(new DPProcess(processRes.getLocalName()));
        }

        // Query and parse Links
        String linkQuery =
                "PREFIX dpol: <" + NAMESPACE + "> " +
                        "SELECT ?link WHERE { ?composition dpol:hasLink ?link . }";
        ResultSet linkResults = executeQuery(model, linkQuery);
        while (linkResults.hasNext()) {
            QuerySolution sol = linkResults.nextSolution();
            Resource linkRes = sol.getResource("link");
            links.add(new DPLink(linkRes.getLocalName()));
        }

        // Create and return the Composition object
        return new DPComposition(components, transports, processes, links);
    }

    private static ResultSet executeQuery(Model model, String queryString) {
        Query query = QueryFactory.create(queryString);
        QueryExecution qexec = QueryExecutionFactory.create(query, model);
        return qexec.execSelect();
    }

    public static void main(String[] args) {
        DPComposition composition = parseOntology();

        // Print the parsed Composition to verify the output
        System.out.println("Components:");
        for (DPComponent component : composition.getComponents()) {
            System.out.println(" - " + component.getComponentName());
        }

        System.out.println("Transports:");
        for (DPTransport transport : composition.getTransports()) {
            System.out.println(" - " + transport.getTransportName());
        }

        System.out.println("Processes:");
        for (DPProcess process : composition.getProcesses()) {
            System.out.println(" - " + process.getProcessName());
        }

        System.out.println("Links:");
        for (DPLink link : composition.getLinks()) {
            System.out.println(" - " + link.getLinkName());
        }
    }
}
