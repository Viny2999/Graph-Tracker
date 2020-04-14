package br.radixeng.service;

import br.radixeng.model.*;
import br.radixeng.repository.GraphRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class GraphService {
    @Autowired
    private GraphRepository repository;

    @Autowired
    private DijkstraService dijkstraService;

    public ResponseEntity<List> findAll() {
        try {
            return ResponseEntity.ok(repository.findAll());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    public ResponseEntity<GraphArray> findById(long id) {
        try {
            Optional<GraphArray> graphOptional = repository.findById(id);
            return graphOptional.isPresent() ?
                    ResponseEntity.ok(graphOptional.get()) :
                    ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
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

    public ResponseEntity getAvailableRoute(long id, String town1, String town2, Long maxStops) {
        try {
            Optional<GraphArray> graphOptional = repository.findById(id);
            if(graphOptional.isPresent()) {
                List<Graph> graphDistinct = graphOptional.get().getData().stream().filter(dijkstraService.distinctByKey(v -> v.getSource())).collect(Collectors.toList());
                int size = graphDistinct.size();
                Path path = new Path(size);

                return ResponseEntity.ok(path.findAllPaths(graphOptional.get(), graphDistinct, town1, town2, maxStops));
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    public ResponseEntity minimumRoute(long id, String town1, String town2) {
        try {
            Optional<GraphArray> graphOptional = repository.findById(id);
            if(graphOptional.isPresent()) {
                String minimumRoute = dijkstraService.executeDijkstra(graphOptional.get(), town1, town2);
                return ResponseEntity.ok(minimumRoute);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
