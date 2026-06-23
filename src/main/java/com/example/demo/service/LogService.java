package com.example.demo.service;

import com.example.demo.dto.LogEventDTO;
import com.example.demo.entity.LogEvent;
import com.example.demo.entity.User;
import com.example.demo.repository.LogRepository;
import com.example.demo.request.LogRequest;
import com.example.demo.response.LogResponse;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
public class LogService {
    private final LogRepository logRepository;
    private final JwtService jwtService;

    @CacheEvict(value = {"logsByUser"}, allEntries = true)
    public LogEventDTO createLogEvent(LogRequest request, String auth){
        Integer userId = jwtService.extractUserId(auth);
        LogEvent logEvent = LogEvent
                .builder()
                .message(request.getMessage())
                .createdAt(LocalDateTime.now())
                .user(User.builder().id(userId).build())
                .build();

        LogEvent created = logRepository.save(logEvent);
        return mapToDto(created);
    }

    @Cacheable(value = "logsByUser", key = "#userId")
    public List<LogEventDTO> getAllLogsOfUserWithId(Integer userId, String auth) {
        Integer foundId = jwtService.extractUserId(auth);
        if (!foundId.equals(userId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
        return logRepository.findByUserId(userId).stream().map(this::mapToDto).toList();
    }

    private LogEventDTO mapToDto(LogEvent logEvent){
        return LogEventDTO
                .builder()
                .id(logEvent.getId())
                .message(logEvent.getMessage())
                .createdAt(logEvent.getCreatedAt())
                .email(logEvent.getUser().getEmail())
                .build();
    }
}
