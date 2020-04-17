package br.radixeng.service;

import br.radixeng.model.*;
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
        List<Vertex> menorCaminho = new ArrayList<Vertex>();
        Vertex vertexCaminho = new Vertex();
        Vertex atual = new Vertex();
        Vertex vizinho = new Vertex();
        List<Vertex> naoVisitados = new ArrayList<Vertex>();

        menorCaminho.add(v1);

		for (int i = 0; i < graph.getVertex().size(); i++) {
			if (graph.getVertex().get(i).getDescription().equals(v1.getDescription())) {
				graph.getVertex().get(i).setDistance(0);
			} else {
				graph.getVertex().get(i).setDistance(9999);
			}
			naoVisitados.add(graph.getVertex().get(i));
		}

		Collections.sort(naoVisitados);

		while (!naoVisitados.isEmpty()) {
            atual = naoVisitados.get(0);
			for (int i = 0; i < atual.getEdges().size(); i++) {
				vizinho = atual.getEdges().get(i).getDestination();
				if (!vizinho.getVisited()) {
					if (vizinho.getDistance() > (atual.getDistance() + atual.getEdges().get(i).getWeight())) {
						vizinho.setDistance(atual.getDistance() + atual.getEdges().get(i).getWeight());
						vizinho.setFather(atual);

						if (vizinho == v2) {
							menorCaminho.clear();
							vertexCaminho = vizinho;
							menorCaminho.add(vizinho);
							while (vertexCaminho.getFather() != null) {
								menorCaminho.add(vertexCaminho.getFather());
								vertexCaminho = vertexCaminho.getFather();
							}
							Collections.sort(menorCaminho);
						}
					}
				}
			}
			atual.setVisited();
			naoVisitados.remove(atual);
			Collections.sort(naoVisitados);
		}
		return menorCaminho;
    }

    public static <T> Predicate<T> distinctByKey(Function<? super T, Object> keyExtractor) {
        Map<Object, Boolean> map = new ConcurrentHashMap<>();
        return t -> map.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
    }
}