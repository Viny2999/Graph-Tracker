package br.graph.model;

public class Edge {

    private int weight;
    private Vertex source;
    private Vertex destination;

    public Edge(int weight, Vertex v1, Vertex v2) {
        this.weight = weight;
        this.source = v1;
        this.destination = v2;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public int getWeight() {
        return weight;
    }

    public void setDestination(Vertex destination) {
        this.destination = destination;
    }

    public Vertex getDestination() {
        return destination;
    }

    public void setSource(Vertex source) {
        this.source = source;
    }

    public Vertex getSource() {
        return source;
    }
}