package com.example.demo.dto;

import lombok.Builder;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Builder
public class LogEventDTO  implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private Long id;

    private String message;

    private LocalDateTime createdAt;

    private String email;
}
