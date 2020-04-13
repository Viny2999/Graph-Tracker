package br.radixeng.service;

import br.radixeng.model.GraphArray;
import br.radixeng.repository.GraphRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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

    public ResponseEntity getAvailableRoute(long id, String town1, String town2, Long maxStops) {
        System.out.println(id + " " + town1 + " " + town2 + " " + maxStops);





        return ResponseEntity.ok().build();
    }

}
