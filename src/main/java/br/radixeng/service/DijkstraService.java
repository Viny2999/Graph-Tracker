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

    public String executeDijkstra(GraphArray graphArray, String v1, String v2) throws JSONException {
        Grafo grafo = prepareDijkstra(graphArray, v1, v2);
        List<Vertice> minimumRoute = minimumRoute(grafo, grafo.encontrarVertice(v1), grafo.encontrarVertice(v2));

        int distance = minimumRoute.get(minimumRoute.size() - 1).getDistancia();
        List<String> paths = minimumRoute.stream().map(v -> v.getDescricao()).collect(Collectors.toList());

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

    private Grafo prepareDijkstra(GraphArray graphArray, String v1, String v2) {
        Grafo grafo = new Grafo();
        Grafo grafoAux = new Grafo();
        List<Graph> sourcesAux = graphArray.getData().stream().filter(distinctByKey(v -> v.getSource())).collect(Collectors.toList());

        for (Graph graph : sourcesAux) {
            Vertice vertice = new Vertice();
            vertice.setDescricao(graph.getSource());
            vertice.setPai(null);
            vertice.setDistancia(0);

            grafoAux.adicionarVertice(vertice);
        }

        for (Vertice vertice : grafoAux.getVertices()) {
            List<Aresta> aresta = new ArrayList<Aresta>();
            for (Graph graph : graphArray.getData()) {
                if(vertice.getDescricao() == graph.getSource()) {
                    aresta.add(new Aresta(graph.getDistance().intValue(), grafoAux.encontrarVertice(graph.getSource()), grafoAux.encontrarVertice(graph.getTarget())));
                }
            }
            vertice.setArestas(aresta);
            grafo.adicionarVertice(vertice);
        }

        return grafo;
    }

    private List<Vertice> minimumRoute(Grafo grafo, Vertice v1, Vertice v2) {
        List<Vertice> menorCaminho = new ArrayList<Vertice>();
        Vertice verticeCaminho = new Vertice();
        Vertice atual = new Vertice();
        Vertice vizinho = new Vertice();
        List<Vertice> naoVisitados = new ArrayList<Vertice>();

        menorCaminho.add(v1);

		for (int i = 0; i < grafo.getVertices().size(); i++) {
			if (grafo.getVertices().get(i).getDescricao().equals(v1.getDescricao())) {
				grafo.getVertices().get(i).setDistancia(0);
			} else {
				grafo.getVertices().get(i).setDistancia(9999);
			}
			naoVisitados.add(grafo.getVertices().get(i));
		}

		Collections.sort(naoVisitados);

		while (!naoVisitados.isEmpty()) {
            atual = naoVisitados.get(0);
			for (int i = 0; i < atual.getArestas().size(); i++) {
				vizinho = atual.getArestas().get(i).getDestino();
				if (!vizinho.verificarVisita()) {
					if (vizinho.getDistancia() > (atual.getDistancia() + atual.getArestas().get(i).getPeso())) {
						vizinho.setDistancia(atual.getDistancia() + atual.getArestas().get(i).getPeso());
						vizinho.setPai(atual);

						if (vizinho == v2) {
							menorCaminho.clear();
							verticeCaminho = vizinho;
							menorCaminho.add(vizinho);
							while (verticeCaminho.getPai() != null) {
								menorCaminho.add(verticeCaminho.getPai());
								verticeCaminho = verticeCaminho.getPai();
							}
							Collections.sort(menorCaminho);
						}
					}
				}
			}
			atual.visitar();
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