package com.example.demo.controllers;

import com.example.demo.dto.SourceRequest;
import com.example.demo.entity.Source;
import com.example.demo.service.SourceService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/sources")
public class SourceController {

    private final SourceService sourceService;
    public SourceController(SourceService sourceService){ this.sourceService = sourceService; }

    @PostMapping
    public ResponseEntity<Source> create(@RequestBody SourceRequest source) {
        return ResponseEntity.ok(sourceService.create(source));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Source> update(@PathVariable Integer id, @RequestBody SourceRequest source) {
        return sourceService.update(id, source)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        sourceService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/sheet/{id}")
    public ResponseEntity<List<Source>> getSourcesOfSheet(@PathVariable Integer id){
        List<Source> sources = sourceService.getSourcesFromSheetId(id);
        return ResponseEntity.ok(sources);
    }
}
