package br.radixeng.model;


import br.radixeng.service.DijkstraService;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    public String findAllPaths(GraphArray graphArray, List<Graph> graphDistinct, String v1, String v2, Long maxStops) throws JSONException {
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

        return jsonParse(pathsFound, maxStops);
    }

    private String jsonParse(List<String> paths, Long maxStops) throws JSONException {
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

        JSONObject responseJson = new JSONObject();
        JSONArray routesJson = new JSONArray();
        for (String path : paths) {
            JSONObject route = new JSONObject();
            route.put("route", path);
            route.put("stops", (path.length() - 1));
            routesJson.put(route);
        }

        responseJson.put("routes", routesJson);

        return responseJson.toString();
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