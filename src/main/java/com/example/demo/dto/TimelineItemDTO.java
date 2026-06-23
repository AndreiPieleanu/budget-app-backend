package com.example.demo.dto;

import com.example.demo.entity.Type;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
public class TimelineItemDTO {
    private Integer id;
    private String description;
    private Type type;
    private BigDecimal amount;
    private String currency;
    private LocalDate startDate;
    private LocalDate endDate;
}
