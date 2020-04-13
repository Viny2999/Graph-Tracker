package br.radixeng.service;

import br.radixeng.model.*;
import br.radixeng.repository.GraphRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Service
public class GraphService {
    @Autowired
    private GraphRepository repository;

    public List findAll() {
        return repository.findAll();
    }

    public ResponseEntity<GraphArray> findById(long id) {
        Optional<GraphArray> graphOptional = repository.findById(id);
        return graphOptional.isPresent() ?
                ResponseEntity.ok(graphOptional.get()) :
                ResponseEntity.notFound().build();
    }

    public ResponseEntity<GraphArray> create(GraphArray graph) {
        GraphArray graphResponse;
        try {
            graphResponse = repository.save(graph);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(graphResponse);
    }

    public ResponseEntity getAvailableRoute(long id, String town1, String town2) {
        Optional<GraphArray> graphOptional = repository.findById(id);
        if(graphOptional.isPresent()) {
            String result = executeDijkstra(graphOptional.get(), town1, town2);
            return ResponseEntity.ok().body(result);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    private String executeDijkstra(GraphArray graphArray, String v1, String v2) {
        Grafo grafo = prepareDijkstra(graphArray, v1, v2);
        DijkstraService dijkstraService = new DijkstraService();
        List<Vertice> minimumRoute = dijkstraService.minimumRoute(grafo, grafo.encontrarVertice(v1), grafo.encontrarVertice(v2));

        int distance = minimumRoute.get(minimumRoute.size() - 1).getDistancia();
        List<String> paths = minimumRoute.stream().map(v -> v.getDescricao()).collect(Collectors.toList());

        return mountResponse(distance, paths);
    }

    private String mountResponse(int distance, List<String> paths) {
        StringBuilder sb = new StringBuilder();
        sb.append("{\"distance\": "+ distance + " , \"path\": [");
        for (String path : paths) {
            sb.append("\"" + path + "\",");
        }
        sb = sb.deleteCharAt(sb.toString().length() -1);
        sb.append("]}");
        return sb.toString();
    }

    private Grafo prepareDijkstra(GraphArray graphArray, String v1, String v2) {
        List<Vertice> verticesAux = new ArrayList<Vertice>();
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

    private static <T> Predicate<T> distinctByKey(Function<? super T, Object> keyExtractor) {
        Map<Object, Boolean> map = new ConcurrentHashMap<>();
        return t -> map.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
    }
}
