package br.radixeng.model;

import java.util.ArrayList;
import java.util.List;

public class Vertex implements Comparable<Vertex>{

    private String description;
    private int distance;
    private boolean visited = false;
    private Vertex father;
    private List<Edge> edges = new ArrayList<Edge>();
    private List<Vertex> neighbors = new ArrayList<Vertex>();

    public void setDescription(String name){
        this.description = name;
    }

    public String getDescription(){
        return description;
    }

    public void setVisited(){
        this.visited = true;
    }

    public boolean getVisited(){
        return visited;
    }

    public void setDistance(int distance){
        this.distance = distance;
    }

    public int getDistance(){
        return this.distance;
    }

    public void setFather(Vertex father){
        this.father = father;
    }

    public Vertex getFather(){
        return this.father;
    }

    public void setNeighbors(List<Vertex> neighbors) {
        this.neighbors.addAll(neighbors);
    }

    public List<Vertex> getNeighbors(){
        return this.neighbors;
    }

    public void setEdges(List <Edge> edges){
        this.edges.addAll(edges);
    }

    public List<Edge> getEdges() {
        return edges;
    }

    public int compareTo(Vertex vertex) {
        if(this.getDistance() < vertex.getDistance()) return -1;
        else if(this.getDistance() == vertex.getDistance()) return 0;
        return 1;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof Vertex){
            Vertex vRef = (Vertex) obj;
            if(this.getDescription().equals(vRef.getDescription())) return true;
        }
        return false;
    }

    @Override
    public String toString() {
        String s = " ";
        s+= this.getDescription();
        return s;
    }
}