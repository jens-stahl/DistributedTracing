package de.stahl.model;

import java.util.*;
import java.util.stream.Collectors;

public class Graph {

    private Map<Node, List<Edge>> graphToplogy;

    public Graph(String graphInputString) {
        this.graphToplogy = buildGraphTopology(buildEdges(graphInputString));
    }

    private Map<Node, List<Edge>> buildGraphTopology(List<Edge> edges) {
        Map<Node, List<Edge>> graph = new HashMap<>();
        for (Edge edge : edges) {
            List<Edge> targetNodes = graph.get(edge.getSource());
            if (targetNodes == null) {
                targetNodes = new ArrayList();
                graph.put(edge.getSource(), targetNodes);

            }
            targetNodes.add(edge);
        }
        return graph;
    }

    private List<Edge> buildEdges(String graphInputString) {
        String[] split = graphInputString.split(",");
        return Arrays.stream(split).map(entry -> new Edge(new Node(entry.substring(0, 1)), new Node(entry.substring(1, 2)), Integer.valueOf(entry.substring(2, 3))))
                .collect(Collectors.toList());
    }

    public int evaluateLatency(Node source, Node target)    {
        List<Edge> edges = getGraphToplogy().get(source);
        Edge edge = edges.stream()
                .filter(filterEdge -> filterEdge.getTarget().equals(target))
                .findFirst()
                .orElse(null);
        if (edge!=null) {
            return edge.getLatency();
        }
        return -1;
    }


    public Map<Node, List<Edge>> getGraphToplogy() {
        return graphToplogy;
    }

    public void setGraphToplogy(Map<Node, List<Edge>> graphToplogy) {
        this.graphToplogy = graphToplogy;
    }
}
