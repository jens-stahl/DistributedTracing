package de.stahl.model;

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
     * @return List of stacks representing the routs found
     */
    List<Stack> evaluateEdges(Node source, Node target, Integer maxHops, Integer hopGoal);

    /**
     * Evaluates minimum latency between two nodes
     *
     * @param source  source node of the route
     * @param target target node of the route
     * @param maxHops maximum of allowed hops
     * @param hopGoal amount of hops to be reached
     * @return minimum latency between the nodes
     */
    int getMinimumLatency(Node source, Node target, Integer maxHops, Integer hopGoal);

    /**
     * Calculates the overall latency of a route
     *
     * @param  nodes  all nodes representing the route
     * @return calculated overall latency of the provided route
     */
    int evaluateOverallLatency(Node... nodes);
    /**
     * Receive the topology of the graph including all nodes and edges
     *
     * @return graph topology
     */
    Map<Node, List<Edge>> getGraphToplogy();
    /**
     * Counts all paths that have a maximum latency of maxLatency
     * @param source  source node of the route
     * @param target target node of the route
     * @param maxHops maximum of allowed hops
     * @param hopGoal amount of hops to be reached
     * @param maxLatency maximum latency the routes may have
     *
     * @return amount of paths with max latency
     */
    int countPathsWithMaxLatency(Node source, Node target, Integer maxHops, Integer hopGoal, int maxLatency);

}
