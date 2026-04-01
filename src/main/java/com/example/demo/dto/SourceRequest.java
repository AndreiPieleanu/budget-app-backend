package com.example.demo.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class SourceRequest {
    private String type;
    private BigDecimal amount;
    private String description;
    private Integer sheetId;
}
