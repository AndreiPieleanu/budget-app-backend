package com.example.demo.service;

import com.example.demo.dto.SheetDTO;
import com.example.demo.dto.SheetRequest;
import com.example.demo.entity.Sheet;
import com.example.demo.entity.User;
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

    @CacheEvict(value = {"sheets", "sheet", "userSheets"}, allEntries = true)
    public SheetDTO create(SheetRequest request) {
        Sheet sheet = Sheet.builder().name(request.name).createdAt(LocalDateTime.now()).user(User.builder().id(request.userId).build()).build();
        Sheet createdSheet = sheetRepository.save(sheet);
        return mapToDto(createdSheet);
    }

    @CacheEvict(value = {"sheets", "sheet", "userSheets"}, allEntries = true)
    public Optional<SheetDTO> update(Integer id, SheetRequest updated) {
        return sheetRepository.findById(id).map(sheet -> {
            sheet.setName(updated.getName());
            Sheet updatedSheet = sheetRepository.save(sheet);
            return mapToDto(updatedSheet);
        });
    }

    @CacheEvict(value = {"sheets", "sheet", "userSheets"}, allEntries = true)
    public void delete(Integer id) {
        sheetRepository.deleteById(id);
    }

    @Cacheable(value = "sheets")
    public List<SheetDTO> getAllSheets(){
        return sheetRepository.findAll().stream().map(this::mapToDto).toList();
    }

    @Cacheable(value = "sheet", key = "#id")
    public SheetDTO getSheet(Integer id) {
        Sheet s = sheetRepository.getReferenceById(id);
        return mapToDto(s);
    }

    @Cacheable(value = "userSheets", key = "#userId")
    public List<SheetDTO> getAllSheetsByUserId(Integer userId){
        return sheetRepository.findByUserId(userId)
                .stream()
                .map(this::mapToDto)
                .toList();
    }

    private SheetDTO mapToDto(Sheet s){
        return SheetDTO
                .builder()
                .id(s.getId())
                .name(s.getName())
                .userId(s.getUser().getId())
                .createdAt(s.getCreatedAt())
                .build();
    }
}
