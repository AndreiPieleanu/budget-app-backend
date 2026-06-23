package com.example.demo.controllers;

import com.example.demo.dto.LogEventDTO;
import com.example.demo.request.LogRequest;
import com.example.demo.response.LogResponse;
import com.example.demo.service.LogService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/logs")
@AllArgsConstructor
public class LogController {
    private final LogService logService;

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<LogEventDTO>> getLogsByUser(
            @PathVariable Integer userId,
            @RequestHeader("Authorization") String auth){
        return ResponseEntity.ok(logService.getAllLogsOfUserWithId(userId, auth));
    }

    @PostMapping
    public ResponseEntity<LogEventDTO> createLog(@RequestBody LogRequest request,
                                                 @RequestHeader("Authorization") String auth){
        return ResponseEntity.ok(logService.createLogEvent(request, auth));
    }
}
