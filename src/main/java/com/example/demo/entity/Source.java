package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "sources")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Source {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Enumerated(EnumType.STRING)
    private Type type;

    private BigDecimal amount;

    private String description;

    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "sheet_id", nullable = false)
    private Sheet sheet;

    @Column(length = 3)
    private String currency = "HUF"; // EUR, USD, RON
}
