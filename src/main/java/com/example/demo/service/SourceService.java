package com.example.demo.service;

import com.example.demo.dto.SourceDTO;
import com.example.demo.request.SourceRequest;
import com.example.demo.entity.Sheet;
import com.example.demo.entity.Source;
import com.example.demo.entity.Type;
import com.example.demo.repository.SheetRepository;
import com.example.demo.repository.SourceRepository;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@AllArgsConstructor
public class SourceService {
    private final SourceRepository sourceRepository;
    private final SheetRepository sheetRepository;
    private final JwtService jwtService;
    private final ExchangeRateService exchangeRateService;

    @CacheEvict(value = {"sourcesBySheet", "allSources", "currencySources"}, allEntries = true)
    public SourceDTO create(SourceRequest request, String auth) {
        Sheet sheet = sheetRepository.findById(request.getSheetId())
                .orElseThrow(() -> new RuntimeException("Sheet not found"));
        Integer userId = jwtService.extractUserId(auth);
        if(!sheet.getUser().getId().equals(userId)){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }

        Source source = Source.builder()
                .type(Type.valueOf(request.getType()))
                .amount(request.getAmount())
                .description(request.getDescription())
                .createdAt(LocalDateTime.now())
                .sheet(sheet)
                .currency(request.getCurrency())
                .build();

        Source created = sourceRepository.save(source);

        return mapToDTO(created);
    }

    @CacheEvict(value = {"sourcesBySheet", "allSources", "currencySources"}, allEntries = true)
    public Optional<SourceDTO> update(Integer id, SourceRequest updated, String auth) {
        Integer userId = jwtService.extractUserId(auth);
        Optional<Source> optional = sourceRepository.findById(id);

        if (optional.isEmpty()) return Optional.empty();

        Source source = optional.get();
        if(!source.getSheet().getUser().getId().equals(userId)){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
        source.setType(Type.valueOf(updated.getType()));
        source.setAmount(updated.getAmount());
        source.setDescription(updated.getDescription());
        source.setCurrency(updated.getCurrency());

        Source saved = sourceRepository.save(source);

        return Optional.of(mapToDTO(saved));
    }

    @CacheEvict(value = {"sourcesBySheet", "allSources", "currencySources"}, allEntries = true)
    public void delete(Integer id, String auth) {
        Source source = sourceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Source not found"));

        Integer userId = jwtService.extractUserId(auth);
        if(!source.getSheet().getUser().getId().equals(userId)){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
        sourceRepository.deleteById(id);
    }

    @Cacheable(value = "sourcesBySheet", key = "#sheetId")
    public List<SourceDTO> getSourcesFromSheetId(Integer sheetId, String auth){
        Integer userId = jwtService.extractUserId(auth);
        sheetRepository.findById(sheetId).ifPresentOrElse((s) -> {
            if(!s.getUser().getId().equals(userId)){
                throw new ResponseStatusException(HttpStatus.FORBIDDEN);
            }
        }, () -> {throw new ResponseStatusException(HttpStatus.NOT_FOUND);});
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
                .currency(s.getCurrency())
                .build();
    }

    @Cacheable(value = "allSources")
    public List<SourceDTO> getAllSources(String auth) {
        String role = jwtService.extractUserRole(auth);
        if(!role.equalsIgnoreCase("admin")){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
        return sourceRepository.findAll().stream().map(this::mapToDTO).toList();
    }

    @Cacheable(value = "currencySources")
    public List<SourceDTO> getConvertedSources(String sheetId, String currencyTo, String auth){
        Integer userId = jwtService.extractUserId(auth);
        Sheet sheet = sheetRepository.getReferenceById(Integer.parseInt(sheetId));
        if(!sheet.getUser().getId().equals(userId)){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
        List<SourceDTO> sources = sourceRepository.findBySheetId(sheet.getId()).stream().map(this::mapToDTO).toList();
        List<SourceDTO> result = exchangeRateService.convertSources(sources, currencyTo);
        return result;
    }
}
