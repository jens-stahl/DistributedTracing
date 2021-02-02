package de.stahl.model;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class Graph implements GraphAPI {

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

    public int getLatencyFromEdges(Stack<Edge> edges, Edge additionalEdgeToCheck) {
        int latency = 0;
        for (Edge edge : edges) {
            latency += edge.getLatency();
        }
        if (additionalEdgeToCheck != null) {
            latency += additionalEdgeToCheck.getLatency();
        }
        return latency;
    }

    public List<List<Node>> getNodeListsFromEdges(List<Stack> allConnectionPaths) {
        List<List<Node>> result = new ArrayList<>();
        for (Stack<Edge> connectionPath : allConnectionPaths) {
            List<Node> nodes = new ArrayList<>();
            result.add(nodes);
            List<Node> nodesFromEdges = getNodesFromEdges(connectionPath);
            for (Node nodeFromEdge : nodesFromEdges) {
                nodes.add(nodeFromEdge);
            }
        }
        return result;
    }

    public int getMinimumLatency(Node source, Node target, Integer maxHops, Integer hopGoal) {
        return getMinimumLatency(evaluateEdges(source,target,maxHops,hopGoal));
    }

    private int getMinimumLatency(List<Stack> allConnectionPaths) {
        int minimumLatency = -1;
        for (Stack allConnectionPath : allConnectionPaths) {
            int currentLatency = getLatencyFromEdges(allConnectionPath, null);
            if (minimumLatency == -1 || currentLatency < minimumLatency) {
                minimumLatency = currentLatency;
            }
        }
        return minimumLatency;
    }

    public int countPathsWithMaxLatency(Node source, Node target, Integer maxHops, Integer hopGoal, int maxLatency)  {
        return countPathsWithMaxLatency(evaluateEdges(source,target,maxHops,hopGoal),maxLatency);
    }

    private int countPathsWithMaxLatency(List<Stack> allConnectionPaths, int maxLatency)  {
        Integer latency = 0;
        AtomicInteger counter = checkLatency(allConnectionPaths, maxLatency, latency, new AtomicInteger(0));
        return counter.get();
    }

    private AtomicInteger checkLatency(List<Stack> allConnectionPaths, int maxLatency, Integer latency, AtomicInteger counter) {
        for (Stack pathToCheck : allConnectionPaths) {
            int currentLatency = getLatencyFromEdges(pathToCheck, null);
            if (latency + currentLatency > maxLatency) {
                continue;
            }
            if (latency + currentLatency < maxLatency) {
                counter.incrementAndGet();
                checkLatency(allConnectionPaths, maxLatency, latency+currentLatency, counter);
            }
        }
        return counter;
    }

    public List<Stack> evaluateEdges(Node source, Node target, Integer maxHops, Integer hopGoal) {
        if (source == null || target == null) {
            throw new RuntimeException("Amount of routes cannot be evaluated! Please provide source and target node");
        }
        Stack<Edge> connectionPath = new Stack();
        List<Stack> allConnectionPaths = new ArrayList<>();
        evaluateEdge(source, target, connectionPath, allConnectionPaths, maxHops, hopGoal);
        return allConnectionPaths;
    }

    private Integer evaluateEdge(Node currentSource, Node targetOfRoute, Stack<Edge> connectionPath, List<Stack> allConnectionPaths, Integer maxHops, Integer hopGoal) {
        List<Edge> edges = getGraphToplogy().get(currentSource);
        if (edges == null) {
            return null;
        }
        for (Edge edge : edges) {
            Node targetEdgeToCheck = edge.getTarget();
            boolean finished = false;
            //can we stop here?
            if (targetEdgeToCheck.equals(targetOfRoute)) {
                finished = true;
            }
            if (maxHops != null && connectionPath.size() > maxHops - 1) {
                //too many hops, continue
                continue;
            }
            if (hopGoal != null && connectionPath.size() < hopGoal - 1) {
                //hop goal not reached yet
                finished = false;
            } else if (hopGoal != null && connectionPath.size() > hopGoal - 1) {
                //too many hops, continue
                continue;
            }
            if (finished) {
                //Found a targetnode!
                addStackToAllConnectionPaths(connectionPath, allConnectionPaths, edge);
            } else {
                //not found keep on going!
                connectionPath.push(edge);
                evaluateEdge(edge.getTarget(), targetOfRoute, connectionPath, allConnectionPaths, maxHops, hopGoal);
                connectionPath.pop();
            }
        }
        return allConnectionPaths.size();
    }

    private List<Node> getNodesFromEdges(Stack<Edge> edges) {
        Stack<Edge> stackToCheck = (Stack<Edge>) edges.clone();
        List<Node> nodes = new ArrayList<>();
        if (stackToCheck == null || stackToCheck.size() == 0) {
            return null;
        }
        Edge lastEdge = stackToCheck.pop();
        nodes.add(0, (lastEdge.getTarget()));
        nodes.add(0, (lastEdge.getSource()));
        while (stackToCheck.size() > 0) {
            nodes.add(0, stackToCheck.pop().getSource());
        }
        return nodes;
    }


    private void addStackToAllConnectionPaths
            (Stack<Edge> connectionPath, List<Stack> allConnectionPaths, Edge targetEdge) {
        Stack<Edge> temp = createTmpStack(connectionPath, targetEdge);
        allConnectionPaths.add(temp);
    }

    private Stack<Edge> createTmpStack(Stack<Edge> connectionPath, Edge targetEdge) {
        Stack<Edge> temp = new Stack();
        for (Edge myEdge : connectionPath) {
            temp.add(myEdge);
        }
        temp.add(targetEdge);
        return temp;
    }

    public int evaluateOverallLatency(Node... nodes) {
        int overallLatency = 0;
        if (nodes == null || nodes.length < 2) {
            throw new RuntimeException("Latency cannot be evaluated! Provide at least two nodes!");
        }
        Iterator<Node> iterator = Arrays.stream(nodes).iterator();
        Node sourceNode = iterator.next();
        Node targetNode = iterator.next();
        Edge edge = getEdgeByNodes(sourceNode, targetNode);
        checkEdge(edge);
        overallLatency += edge.getLatency();
        while (iterator.hasNext()) {
            sourceNode = targetNode;
            targetNode = iterator.next();
            edge = getEdgeByNodes(sourceNode, targetNode);
            checkEdge(edge);
            overallLatency += edge.getLatency();
        }
        return overallLatency;
    }

    private void checkEdge(Edge edge) {
        if (edge == null) {
            throw new RuntimeException("NO SUCH TRACE");
        }
    }

    private Edge getEdgeByNodes(Node source, Node target) {
        List<Edge> edges = getGraphToplogy().get(source);
        return edges.stream()
                .filter(filterEdge -> filterEdge.getTarget().equals(target))
                .findFirst()
                .orElse(null);
    }

    public Map<Node, List<Edge>> getGraphToplogy() {
        return graphToplogy;
    }

}
