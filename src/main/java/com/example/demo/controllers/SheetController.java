package com.example.demo.controllers;

import com.example.demo.dto.SheetDTO;
import com.example.demo.dto.SheetRequest;
import com.example.demo.entity.Sheet;
import com.example.demo.service.SheetService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/sheets")
public class SheetController {
    private final SheetService sheetService;

    public SheetController(SheetService sheetService) {
        this.sheetService = sheetService;
    }

    @PostMapping
    public ResponseEntity<SheetDTO> create(@RequestBody SheetRequest request) {
        return ResponseEntity.ok(sheetService.create(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<SheetDTO> update(@PathVariable Integer id, @RequestBody SheetRequest request) {
        return sheetService.update(id, request)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        sheetService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<SheetDTO>> getAllSheets(){
        return ResponseEntity.ok(sheetService.getAllSheets());
    }

    @GetMapping("/{id}")
    public ResponseEntity<SheetDTO> getSheet(@PathVariable Integer id){
        return ResponseEntity.ok(sheetService.getSheet(id));
    }
}
