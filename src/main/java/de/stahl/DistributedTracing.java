package de.stahl;

import de.stahl.model.Graph;
import de.stahl.model.Node;
import de.stahl.util.Util;

public class DistributedTracing {

    public static void main(String[] args) {
        String graphInputString = Util.readInputGraphFromTextFile();
        Graph graph = new Graph(graphInputString);
        System.out.println(graph.evaluateOverallLatency(new Node("A"), new Node("B"), new Node("C")));
        System.out.println(graph.evaluateOverallLatency(new Node("A"), new Node("D")));
        System.out.println(graph.evaluateOverallLatency(new Node("A"), new Node("D"), new Node("C")));
        System.out.println(graph.evaluateOverallLatency(new Node("A"), new Node("E"), new Node("B"), new Node("C"), new Node("D")));
        try {
            System.out.println(graph.evaluateOverallLatency(new Node("A"), new Node("E"), new Node("D")));
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        System.out.println(graph.evaluateEdges(new Node("C"), new Node("C"), 3, null).size());
        System.out.println(graph.evaluateEdges(new Node("A"), new Node("C"), null, 4).size());
        System.out.println(graph.getMinimumLatency(new Node("A"), new Node("C"), null, null));
        System.out.println(graph.getMinimumLatency(new Node("B"), new Node("B"), 10, null));
        System.out.println(graph.countPathsWithMaxLatency(new Node("C"), new Node("C"), null, null,30));
    }
}
