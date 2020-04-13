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
