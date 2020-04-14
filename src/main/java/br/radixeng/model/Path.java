package br.radixeng.model;


import br.radixeng.service.DijkstraService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Path {

    @Autowired
    private DijkstraService dijkstraService;
    private int v;
    private ArrayList<Integer>[] adjList;
    List<String> pathsFound = new ArrayList<>();

    public Path(int vertices){
        this.v = vertices;
        initAdjList();
    }

    private void initAdjList() {
        adjList = new ArrayList[v];

        for(int i = 0; i < v; i++) {
            adjList[i] = new ArrayList<>();
        }
    }

    private void addEdge(int u, int v) {
        adjList[u].add(v);
    }

    public String findAllPaths(GraphArray graphArray, List<Graph> graphDistinct, String v1, String v2, Long maxStops) {
        Map<String, Integer> mapInteger = new HashMap<>();
        Map<Integer, String> mapString = new HashMap<>();

        for (int i = 0; i < graphDistinct.size(); i++) {
            mapInteger.put(graphDistinct.get(i).getSource(), i);
            mapString.put(i, graphDistinct.get(i).getSource());
        }

        for (Graph graph :graphArray.getData()) {
            addEdge(mapInteger.get(graph.getSource()), mapInteger.get(graph.getTarget()));
        }

        printAllPaths(mapInteger.get(v1), mapInteger.get(v2), mapString);

        return mountResponse(pathsFound, maxStops);
    }

    private String mountResponse(List<String> paths, Long maxStops) {
        List<String> pathExcluded = new ArrayList<>();

        if(maxStops != null) {
            for (String path : paths) {
                if ((path.length() - 1) > maxStops) {
                   pathExcluded.add(path);
                }
            }
        }

        for (String path : pathExcluded) {
            paths.remove(path);
        }

        StringBuilder sb = new StringBuilder();
        sb.append("{\"routes\": [");
        for (String path : paths) {
            sb.append("{\"routes\": \"" + path + "\",");
            sb.append("\"stops\": " + (path.length() - 1) + "}");
        }
        sb = sb.deleteCharAt(sb.toString().length() -1);
        sb.append("]}");
        return sb.toString();
    }

    private void printAllPaths(int s, int d, Map<Integer, String> map) {
        boolean[] isVisited = new boolean[v];
        ArrayList<Integer> pathList = new ArrayList<>();
        pathList.add(s);
        printAllPathsUtil(s, d, isVisited, pathList, map);
    }

    private void printAllPathsUtil(Integer u, Integer d, boolean[] isVisited, List<Integer> localPathList, Map<Integer, String> map) {
        isVisited[u] = true;

        if (u.equals(d)) {
            StringBuilder sb = new StringBuilder();
            for (Integer path :localPathList) {
                sb.append(map.get(path.intValue()));
            }
            pathsFound.add(sb.toString());
            isVisited[u]= false;
            return;
        }

        for (Integer i : adjList[u]) {
            if (!isVisited[i]) {
                localPathList.add(i);
                printAllPathsUtil(i, d, isVisited, localPathList, map);
                localPathList.remove(i);
            }
        }
        isVisited[u] = false;
    }
}