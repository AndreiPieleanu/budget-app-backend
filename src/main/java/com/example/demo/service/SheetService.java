package com.example.demo.service;

import com.example.demo.dto.SheetDTO;
import com.example.demo.dto.SheetRequest;
import com.example.demo.entity.Sheet;
import com.example.demo.repository.SheetRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
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

    @CacheEvict(value = {"sheets"}, allEntries = true)
    public SheetDTO create(SheetRequest request) {
        Sheet sheet = Sheet.builder().name(request.name).createdAt(LocalDateTime.now()).build();
        Sheet createdSheet = sheetRepository.save(sheet);
        SheetDTO dto = SheetDTO
                .builder()
                .id(createdSheet.getId())
                .name(createdSheet.getName())
                .createdAt(createdSheet.getCreatedAt())
                .build();
        return dto;
    }

    @CacheEvict(value = {"sheets", "sheet"}, allEntries = true)
    public Optional<SheetDTO> update(Integer id, SheetRequest updated) {
        return sheetRepository.findById(id).map(sheet -> {
            sheet.setName(updated.getName());
            Sheet updatedSheet = sheetRepository.save(sheet);
            return SheetDTO
                    .builder()
                    .id(updatedSheet.getId())
                    .name(updatedSheet.getName())
                    .createdAt(updatedSheet.getCreatedAt())
                    .build();
        });
    }

    @CacheEvict(value = {"sheets", "sheet"}, allEntries = true)
    public void delete(Integer id) {
        sheetRepository.deleteById(id);
    }

    @Cacheable(value = "sheets")
    public List<SheetDTO> getAllSheets(){
        return sheetRepository.findAll().stream().map(s -> {
            SheetDTO dto = SheetDTO
                    .builder()
                    .id(s.getId())
                    .name(s.getName())
                    .createdAt(s.getCreatedAt())
                    .build();
            return dto;
        }).toList();
    }

    @Cacheable(value = "sheet", key = "#id")
    public SheetDTO getSheet(Integer id) {
        Sheet s = sheetRepository.getReferenceById(id);
        SheetDTO dto = SheetDTO
                .builder()
                .id(s.getId())
                .name(s.getName())
                .createdAt(s.getCreatedAt())
                .build();
        return dto;
    }
}
