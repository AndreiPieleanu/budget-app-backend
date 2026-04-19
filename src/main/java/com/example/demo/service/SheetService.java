package com.example.demo.service;

import com.example.demo.dto.SheetDTO;
import com.example.demo.dto.SheetRequest;
import com.example.demo.entity.Sheet;
import com.example.demo.entity.User;
import com.example.demo.repository.SheetRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class SheetService {

    private final SheetRepository sheetRepository;
    private final JwtService jwtService;

    public SheetService(SheetRepository sheetRepository, JwtService jwtService) {
        this.sheetRepository = sheetRepository;
        this.jwtService = jwtService;
    }

    @CacheEvict(value = {"sheets", "sheet", "userSheets"}, allEntries = true)
    public SheetDTO create(SheetRequest request, String auth) {
        Integer userId = jwtService.extractUserId(auth);
        Sheet sheet = Sheet.builder().name(request.name).createdAt(LocalDateTime.now()).user(User.builder().id(userId).build()).build();
        Sheet createdSheet = sheetRepository.save(sheet);
        return mapToDto(createdSheet);
    }

    @CacheEvict(value = {"sheets", "sheet", "userSheets"}, allEntries = true)
    public Optional<SheetDTO> update(Integer id, SheetRequest updated, String auth) {
        Integer userId = jwtService.extractUserId(auth);
        return sheetRepository.findById(id).map(sheet -> {
            if (!sheet.getUser().getId().equals(userId)) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN);
            }
            sheet.setName(updated.getName());
            Sheet updatedSheet = sheetRepository.save(sheet);
            return mapToDto(updatedSheet);
        });
    }

    @CacheEvict(value = {"sheets", "sheet", "userSheets"}, allEntries = true)
    public void delete(Integer id, String auth) {
        Integer userId = jwtService.extractUserId(auth);
        sheetRepository.findById(id).ifPresent((s) -> {
            if(!s.getUser().getId().equals(userId)){
                throw new ResponseStatusException(HttpStatus.FORBIDDEN);
            }
            sheetRepository.deleteById(id);
        });
    }

    @Cacheable(value = "sheets")
    public List<SheetDTO> getAllSheets(String auth){
        String role = jwtService.extractUserRole(auth);
        if(!role.equalsIgnoreCase("admin")){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
        return sheetRepository.findAll().stream().map(this::mapToDto).toList();
    }

    @Cacheable(value = "sheet", key = "#id")
    public SheetDTO getSheet(Integer id, String auth) {
        Integer userId = jwtService.extractUserId(auth);
        Sheet s = sheetRepository.getReferenceById(id);
        if(!s.getUser().getId().equals(userId)){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
        return mapToDto(s);
    }

    public List<SheetDTO> getMySheets(String auth){
        Integer userId = jwtService.extractUserId(auth);
        return getSheetsByUserId(userId);
    }

    @Cacheable(value="userSheets", key="#userId")
    public List<SheetDTO> getSheetsByUserId(Integer userId){
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
