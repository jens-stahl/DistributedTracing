package de.stahl.model;

public class Edge {
    private Node source;
    private Node target;
    private int latency;

    public Edge(Node source, Node target, int latency)  {
        this.source=source;
        this.target=target;
        this.latency=latency;
    }

    public Node getSource() {
        return source;
    }

    public void setSource(Node source) {
        this.source = source;
    }

    public Node getTarget() {
        return target;
    }

    public void setTarget(Node target) {
        this.target = target;
    }

    public int getLatency() {
        return latency;
    }

    public void setLatency(int latency) {
        this.latency = latency;
    }
}
