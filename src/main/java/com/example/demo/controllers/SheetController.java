package com.example.demo.controllers;

import com.example.demo.dto.SheetDTO;
import com.example.demo.request.SheetRequest;
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
    public ResponseEntity<SheetDTO> create(@RequestBody SheetRequest request, @RequestHeader("Authorization") String auth) {
        return ResponseEntity.ok(sheetService.create(request, auth));
    }

    @PutMapping("/{id}")
    public ResponseEntity<SheetDTO> update(@PathVariable Integer id, @RequestBody SheetRequest request, @RequestHeader("Authorization") String auth) {
        return sheetService.update(id, request, auth)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id, @RequestHeader("Authorization") String auth) {
        sheetService.delete(id, auth);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<SheetDTO>> getAllSheets(@RequestHeader("Authorization") String auth){
        return ResponseEntity.ok(sheetService.getAllSheets(auth));
    }
    @GetMapping("/me")
    public ResponseEntity<List<SheetDTO>> getMySheets(@RequestHeader("Authorization") String auth){
        return ResponseEntity.ok(sheetService.getMySheets(auth));
    }

    @GetMapping("/{id}")
    public ResponseEntity<SheetDTO> getSheet(@PathVariable Integer id, @RequestHeader("Authorization") String auth){
        return ResponseEntity.ok(sheetService.getSheet(id, auth));
    }
}
