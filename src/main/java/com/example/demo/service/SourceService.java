package com.example.demo.service;

import com.example.demo.dto.SourceDTO;
import com.example.demo.dto.SourceRequest;
import com.example.demo.entity.Sheet;
import com.example.demo.entity.Source;
import com.example.demo.entity.Type;
import com.example.demo.repository.SheetRepository;
import com.example.demo.repository.SourceRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class SourceService {
    private final SourceRepository sourceRepository;
    private final SheetRepository sheetRepository;
    public SourceService(SourceRepository sourceRepository, SheetRepository sheetRepository){
        this.sourceRepository = sourceRepository;
        this.sheetRepository = sheetRepository;
    }

    @CacheEvict(value = {"sourcesBySheet", "allSources"}, allEntries = true)
    public SourceDTO create(SourceRequest request) {

        Sheet sheet = sheetRepository.findById(request.getSheetId())
                .orElseThrow(() -> new RuntimeException("Sheet not found"));

        Source source = Source.builder()
                .type(Type.valueOf(request.getType()))
                .amount(request.getAmount())
                .description(request.getDescription())
                .createdAt(LocalDateTime.now())
                .sheet(sheet)
                .build();

        Source created = sourceRepository.save(source);

        return mapToDTO(created);
    }

    @CacheEvict(value = {"sourcesBySheet", "allSources"}, allEntries = true)
    public Optional<SourceDTO> update(Integer id, SourceRequest updated) {

        Optional<Source> optional = sourceRepository.findById(id);

        if (optional.isEmpty()) return Optional.empty();

        Source source = optional.get();

        source.setType(Type.valueOf(updated.getType()));
        source.setAmount(updated.getAmount());
        source.setDescription(updated.getDescription());

        Source saved = sourceRepository.save(source);

        return Optional.of(mapToDTO(saved));
    }

    @CacheEvict(value = {"sourcesBySheet", "allSources"}, allEntries = true)
    public void delete(Integer id) {
        Source source = sourceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Source not found"));

        Integer sheetId = source.getSheet().getId();
        sourceRepository.deleteById(id);
    }

    @Cacheable(value = "sourcesBySheet", key = "#sheetId")
    public List<SourceDTO> getSourcesFromSheetId(Integer sheetId){
        return sourceRepository
                .findAll()
                .stream()
                .filter(s -> Objects.equals(s.getSheet().getId(), sheetId))
                .map(this::mapToDTO)
                .toList();
    }
    private SourceDTO mapToDTO(Source s){
        return SourceDTO.builder()
                .id(s.getId())
                .sheetId(s.getSheet().getId())
                .type(s.getType())
                .amount(s.getAmount())
                .description(s.getDescription())
                .createdAt(s.getCreatedAt())
                .build();
    }

    @Cacheable(value = "allSources")
    public List<SourceDTO> getAllSources() {
        return sourceRepository.findAll().stream().map(this::mapToDTO).toList();
    }
}
