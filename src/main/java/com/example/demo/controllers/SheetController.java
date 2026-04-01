package com.example.demo.controllers;

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
    public ResponseEntity<Sheet> create(@RequestBody Sheet sheet) {
        return ResponseEntity.ok(sheetService.create(sheet));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Sheet> update(@PathVariable Integer id, @RequestBody Sheet sheet) {
        return sheetService.update(id, sheet)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        sheetService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<Sheet>> getAllSheets(){
        return ResponseEntity.ok(sheetService.getAllSheets());
    }
}
