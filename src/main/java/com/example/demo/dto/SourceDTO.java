package com.example.demo.dto;

import com.example.demo.entity.Type;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Builder;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
public class SourceDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private Integer id;

    @Enumerated(EnumType.STRING)
    private Type type;

    private BigDecimal amount;

    private String description;

    private LocalDateTime createdAt;
    private Integer sheetId;
}
