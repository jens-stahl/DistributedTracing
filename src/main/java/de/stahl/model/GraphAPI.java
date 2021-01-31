package de.stahl.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

/**
 *
 * Interface class specifying the methods available for a Graph
 *
 * @author Jens Stahl
 */
public interface GraphAPI {

    /**
     * Counts all possible Routes between two nodes
     *
     * @param source  source node of the route
     * @param target target node of the route
     * @param maxHops maximum of allowed hops
     * @param hopGoal amount of hops to be reached
     * @return routs found
     */
    public List<Stack> evaluateRoutes(Node source, Node target, Integer maxHops, Integer hopGoal);
    /**
     * Calculates the overall latency of a route
     *
     * @param  nodes  all nodes representing the route
     * @return calculated overall latency of the provided route
     */
    public int evaluateOverallLatency(Node... nodes);
    /**
     * Receive the topology of the graph including all nodes and edges
     *
     * @return graph topology
     */
    public Map<Node, List<Edge>> getGraphToplogy();


}
