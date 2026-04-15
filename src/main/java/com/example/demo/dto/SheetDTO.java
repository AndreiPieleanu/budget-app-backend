package com.example.demo.dto;

import lombok.Builder;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Builder
public class SheetDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    public Integer id;
    public String name;
    private LocalDateTime createdAt;
}
