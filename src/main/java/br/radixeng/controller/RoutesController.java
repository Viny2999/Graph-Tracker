package br.radixeng.controller;

import br.radixeng.service.GraphService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping({"/routes"})
public class RoutesController {
    @Autowired
    private GraphService graphService;

    @PostMapping(path = {"/{id}/from/{town1}/to/{town2}"}, produces = MediaType.APPLICATION_JSON_VALUE)
    public void getAvailableRoute(@PathVariable long id, @PathVariable String town1, @PathVariable String town2, @RequestParam(required = false) Long maxStops) {

    }
}