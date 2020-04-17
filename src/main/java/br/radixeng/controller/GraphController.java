package br.radixeng.controller;

import br.radixeng.model.GraphList;
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
    public ResponseEntity<List> findAll() {
        return graphService.findAll();
    }

    @GetMapping(path = {"/{id}"})
    public ResponseEntity<GraphList> findById(@PathVariable long id) {
       return graphService.findById(id);
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GraphList> create(@RequestBody GraphList data) {
        return graphService.create(data);
    }
}
