package com.example.demo.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "sheets")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Sheet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;

    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "sheet", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Source> sources;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}
