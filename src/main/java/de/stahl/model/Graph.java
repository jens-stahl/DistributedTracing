package de.stahl.model;

import java.util.*;
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

    public List<Stack> evaluateRoutes(Node source, Node target, Integer maxHops, Integer hopGoal)    {
        if (source==null || target==null)    {
            throw new RuntimeException("Amount of routes cannot be evaluated! Please provide source and target node");
        }
        Stack connectionPath = new Stack();
        connectionPath.push(source);
        List<Stack> allConnectionPaths = new ArrayList<>();
        evaluateNode(source, target, connectionPath, allConnectionPaths, maxHops, hopGoal);
        return allConnectionPaths;
    }

    private Integer evaluateNode(Node currentSource, Node targetOfRoute, Stack connectionPath, List<Stack> allConnectionPaths, Integer maxHops, Integer hopGoal) {
        List<Edge> edges = getGraphToplogy().get(currentSource);
        if (edges==null)    {
            return null;
        }
        for (Edge edge : edges) {
            Node targetEdgeToCheck = edge.getTarget();
            boolean finished = false;
            //can we stop here?
            if (targetEdgeToCheck.equals(targetOfRoute))   {
                finished=true;
            }
            if (maxHops!=null && connectionPath.size()>maxHops)  {
                //too many hops, continue
                continue;
            }
            if (hopGoal!=null && connectionPath.size()<hopGoal) {
                //hop goal not reached yet
                finished=false;
            }   else if (hopGoal!=null && connectionPath.size()>hopGoal)    {
                //too many hops, continue
                continue;
            }
            if (finished)   {
                //Found a targetnode!
                addStackToAllConnectionPaths(connectionPath, allConnectionPaths, targetEdgeToCheck);
            }   else    {
                //not found keep on going!
                connectionPath.push(edge.getTarget());
                evaluateNode(edge.getTarget(),targetOfRoute, connectionPath, allConnectionPaths, maxHops, hopGoal);
                connectionPath.pop();
            }
        }
        return allConnectionPaths.size();
    }

    private void addStackToAllConnectionPaths(Stack connectionPath, List<Stack> allConnectionPaths, Node targetEdgeToCheck) {
        Stack temp = new Stack();
        for (Object myNode : connectionPath) {
            temp.add(myNode);
        }
        temp.add(targetEdgeToCheck);
        allConnectionPaths.add(temp);
    }

    public int evaluateOverallLatency(Node... nodes)    {
        int overallLatency=0;
        if (nodes==null || nodes.length<2)    {
            throw new RuntimeException("Latency cannot be evaluated! Provide at least two nodes!");
        }
        Iterator<Node> iterator = Arrays.stream(nodes).iterator();
        Node sourceNode = iterator.next();
        Node targetNode = iterator.next();
        Edge edge = getEdgeByNodes(sourceNode,targetNode);
        checkEdge(edge);
        overallLatency+=edge.getLatency();
        while(iterator.hasNext())    {
            sourceNode = targetNode;
            targetNode = iterator.next();
            edge = getEdgeByNodes(sourceNode,targetNode);
            checkEdge(edge);
            overallLatency+=edge.getLatency();
        }
        return overallLatency;
    }

    private void checkEdge(Edge edge) {
        if (edge==null) {
            throw new RuntimeException("NO SUCH TRACE");
        }
    }

    private Edge getEdgeByNodes(Node source,Node target)  {
        List<Edge> edges = getGraphToplogy().get(source);
        return  edges.stream()
                .filter(filterEdge -> filterEdge.getTarget().equals(target))
                .findFirst()
                .orElse(null);
    }

    public Map<Node, List<Edge>> getGraphToplogy() {
        return graphToplogy;
    }

}
