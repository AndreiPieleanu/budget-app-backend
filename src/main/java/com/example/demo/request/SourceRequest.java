package com.example.demo.request;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class SourceRequest {
    private String type;
    private BigDecimal amount;
    private String description;
    private Integer sheetId;
    private String currency;
    private BigDecimal actualAmount;
    private LocalDate possibleStartDate;
    private LocalDate possibleEndDate;
}
