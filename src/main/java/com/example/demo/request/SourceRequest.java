package com.example.demo.request;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class SourceRequest {
    private String type;
    private BigDecimal amount;
    private String description;
    private Integer sheetId;
    private String currency;
}
