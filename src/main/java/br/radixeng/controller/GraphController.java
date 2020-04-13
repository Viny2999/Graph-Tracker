package br.radixeng.controller;

import br.radixeng.model.GraphArray;
import br.radixeng.service.GraphService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping({"/graph"})
public class GraphController {
    @Autowired
    private GraphService graphService;

    @GetMapping
    public List findAll() {
        return graphService.findAll();
    }

    @GetMapping(path = {"/{id}"})
    public ResponseEntity<GraphArray> findById(@PathVariable long id) {
       return graphService.findById(id);
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GraphArray> create(@RequestBody GraphArray data) {
        return graphService.create(data);
    }

    @PostMapping(path = {"/{id}/from/{town1}/to/{town2}"})
    public ResponseEntity getAvailableRoute(@PathVariable long id, @PathVariable String town1, @PathVariable String town2, @RequestParam(required = false) Long maxStops) {
        return graphService.getAvailableRoute(id, town1, town2, maxStops);
    }
}
