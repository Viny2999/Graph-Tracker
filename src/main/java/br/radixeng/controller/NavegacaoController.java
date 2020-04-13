package br.radixeng.controller;

import br.radixeng.model.NavegacaoInfo;
import br.radixeng.repository.NavegacaoRepository;

import org.aspectj.bridge.MessageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping({"/navegacao"})
public class NavegacaoController {
    @Autowired
    private NavegacaoRepository repository;

    @GetMapping
    public List findAll() {
        return repository.findAll();
    }

    @GetMapping(path = {"/{id}"})
    public ResponseEntity<NavegacaoInfo> findById(@PathVariable long id) {
        Optional<NavegacaoInfo> navegacaoInfoOptional = repository.findById(id);
        return navegacaoInfoOptional.isPresent() ?
                ResponseEntity.ok(navegacaoInfoOptional.get()) :
                ResponseEntity.notFound().build();
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public NavegacaoInfo create(@RequestBody NavegacaoInfo navegacaoInfo) {
        System.out.println(navegacaoInfo);
        return repository.save(navegacaoInfo);
    }
}
