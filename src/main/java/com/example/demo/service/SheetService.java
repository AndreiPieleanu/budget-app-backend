package com.example.demo.service;

import com.example.demo.entity.Sheet;
import com.example.demo.repository.SheetRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class SheetService {

    private final SheetRepository sheetRepository;

    public SheetService(SheetRepository sheetRepository) {
        this.sheetRepository = sheetRepository;
    }

    public Sheet create(Sheet sheet) {
        sheet.setCreatedAt(LocalDateTime.now());
        return sheetRepository.save(sheet);
    }

    public Optional<Sheet> update(Integer id, Sheet updated) {
        return sheetRepository.findById(id).map(sheet -> {
            sheet.setName(updated.getName());
            return sheetRepository.save(sheet);
        });
    }

    public void delete(Integer id) {
        sheetRepository.deleteById(id);
    }

    public List<Sheet> getAllSheets(){
        return sheetRepository.findAll();
    }
}
