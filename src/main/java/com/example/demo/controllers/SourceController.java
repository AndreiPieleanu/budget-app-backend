package com.example.demo.controllers;

import com.example.demo.dto.SourceDTO;
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
    public ResponseEntity<SourceDTO> create(
            @RequestBody SourceRequest source,
            @RequestHeader("Authorization") String auth) {
        return ResponseEntity.ok(sourceService.create(source, auth));
    }

    @PutMapping("/{id}")
    public ResponseEntity<SourceDTO> update(
            @PathVariable Integer id,
            @RequestBody SourceRequest source,
            @RequestHeader("Authorization") String auth) {
        return sourceService.update(id, source, auth)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable Integer id,
            @RequestHeader("Authorization") String auth) {
        sourceService.delete(id, auth);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/sheet/{id}")
    public ResponseEntity<List<SourceDTO>> getSourcesOfSheet(
            @PathVariable Integer id,
            @RequestHeader("Authorization") String auth){
        List<SourceDTO> sources = sourceService.getSourcesFromSheetId(id, auth);
        return ResponseEntity.ok(sources);
    }
    @GetMapping
    public ResponseEntity<List<SourceDTO>> getAllSources(
            @RequestHeader("Authorization") String auth){
        return ResponseEntity.ok(sourceService.getAllSources(auth));
    }
}
