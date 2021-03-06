package br.graph.controller;

import br.graph.service.GraphService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping({"/distance"})
public class DistanceController {
    @Autowired
    private GraphService graphService;

    @PostMapping(path = {"/{id}/from/{town1}/to/{town2}"}, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity minimumRoute(@PathVariable long id, @PathVariable String town1, @PathVariable String town2) {
        return graphService.minimumRoute(id, town1, town2);
    }
}
