package br.graph.service;

import br.graph.model.*;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Service
public class DijkstraService {

    public String executeDijkstra(GraphList graphList, String v1, String v2) throws JSONException {
        Graph graph = prepareDijkstra(graphList, v1, v2);
        List<Vertex> minimumRoute = minimumRoute(graph, graph.findVertex(v1), graph.findVertex(v2));

        int distance = minimumRoute.get(minimumRoute.size() - 1).getDistance();
        List<String> paths = minimumRoute.stream().map(v -> v.getDescription()).collect(Collectors.toList());

        return parseJson(distance, paths);
    }

    private String parseJson(int distance, List<String> paths) throws JSONException {
        JSONObject responseJson = new JSONObject();
        JSONArray pathJson = new JSONArray();

        responseJson.put("distance", distance);

        for (String path : paths) {
            pathJson.put(path);
        }

        responseJson.put("path", pathJson);

        return responseJson.toString();
    }

    private Graph prepareDijkstra(GraphList graphList, String v1, String v2) {
        Graph graph = new Graph();
        Graph graphAux = new Graph();
        List<GraphInfo> sourcesAux = graphList.getData().stream().filter(distinctByKey(v -> v.getSource())).collect(Collectors.toList());

        for (GraphInfo graphInfo : sourcesAux) {
            Vertex vertex = new Vertex();
            vertex.setDescription(graphInfo.getSource());
            vertex.setFather(null);
            vertex.setDistance(0);

            graphAux.addVertex(vertex);
        }

        for (Vertex vertex : graphAux.getVertex()) {
            List<Edge> edge = new ArrayList<Edge>();
            for (GraphInfo graphInfo : graphList.getData()) {
                if(vertex.getDescription() == graphInfo.getSource()) {
                    edge.add(new Edge(graphInfo.getDistance().intValue(), graphAux.findVertex(graphInfo.getSource()), graphAux.findVertex(graphInfo.getTarget())));
                }
            }
            vertex.setEdges(edge);
            graph.addVertex(vertex);
        }

        return graph;
    }

    private List<Vertex> minimumRoute(Graph graph, Vertex v1, Vertex v2) {
        List<Vertex> minimumRoute = new ArrayList<Vertex>();
        Vertex vertexRoute = new Vertex();
        Vertex now = new Vertex();
        Vertex neighbor = new Vertex();
        List<Vertex> noVisited = new ArrayList<Vertex>();

        minimumRoute.add(v1);

		for (int i = 0; i < graph.getVertex().size(); i++) {
			if (graph.getVertex().get(i).getDescription().equals(v1.getDescription())) {
				graph.getVertex().get(i).setDistance(0);
			} else {
				graph.getVertex().get(i).setDistance(9999);
			}
			noVisited.add(graph.getVertex().get(i));
		}

		Collections.sort(noVisited);

		while (!noVisited.isEmpty()) {
            now = noVisited.get(0);
			for (int i = 0; i < now.getEdges().size(); i++) {
				neighbor = now.getEdges().get(i).getDestination();
				if (!neighbor.getVisited()) {
					if (neighbor.getDistance() > (now.getDistance() + now.getEdges().get(i).getWeight())) {
						neighbor.setDistance(now.getDistance() + now.getEdges().get(i).getWeight());
						neighbor.setFather(now);

						if (neighbor == v2) {
							minimumRoute.clear();
							vertexRoute = neighbor;
							minimumRoute.add(neighbor);
							while (vertexRoute.getFather() != null) {
								minimumRoute.add(vertexRoute.getFather());
								vertexRoute = vertexRoute.getFather();
							}
							Collections.sort(minimumRoute);
						}
					}
				}
			}
			now.setVisited();
			noVisited.remove(now);
			Collections.sort(noVisited);
		}
		return minimumRoute;
    }

    public static <T> Predicate<T> distinctByKey(Function<? super T, Object> keyExtractor) {
        Map<Object, Boolean> map = new ConcurrentHashMap<>();
        return t -> map.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
    }
}