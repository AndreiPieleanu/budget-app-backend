package com.example.demo.service;

import com.example.demo.dto.SourceRequest;
import com.example.demo.entity.Sheet;
import com.example.demo.entity.Source;
import com.example.demo.entity.Type;
import com.example.demo.repository.SheetRepository;
import com.example.demo.repository.SourceRepository;
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
    public Source create(SourceRequest request) {
        Sheet sheet = sheetRepository.findById(request.getSheetId())
                .orElseThrow(() -> new RuntimeException("Sheet not found"));

        Source source = Source.builder()
                .type(Type.valueOf(request.getType()))
                .amount(request.getAmount())
                .description(request.getDescription())
                .createdAt(LocalDateTime.now())
                .sheet(sheet)
                .build();

        return sourceRepository.save(source);
    }

    public Optional<Source> update(Integer id, SourceRequest updated) {
        return sourceRepository.findById(id).map(source -> {
            source.setAmount(updated.getAmount());
            source.setType(Type.valueOf(updated.getType()));
            source.setDescription(updated.getDescription());
            return sourceRepository.save(source);
        });
    }

    public void delete(Integer id) {
        sourceRepository.deleteById(id);
    }

    public List<Source> getSourcesFromSheetId(Integer sheetId){
        return sourceRepository.findAll().stream().filter(s -> Objects.equals(s.getSheet().getId(), sheetId)).toList();
    }
}
