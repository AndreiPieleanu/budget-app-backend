package com.example.demo.response;

import com.example.demo.dto.LogEventDTO;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class LogResponse {
    private List<LogEventDTO> events;
}
