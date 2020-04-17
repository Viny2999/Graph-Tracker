package br.radixeng.model;

import java.util.ArrayList;
import java.util.List;

public class Graph {

    private List<Vertex> graph = new ArrayList<Vertex>();

    public void setVertex(List<Vertex> vertexs) {
        this.graph.addAll(vertexs);
    }

    public void addVertex(Vertex newVertex) {
        this.graph.add(newVertex);
    }

    public List<Vertex> getVertex() {
        return this.graph;
    }

    public Vertex findVertex(String nome) {
        for (int i = 0; i < this.getVertex().size(); i++) {
            if (nome.equalsIgnoreCase(this.getVertex().get(i).getDescription())) {
                return this.getVertex().get(i);
            }
        }
        return null;
    }
}