package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Setter
    @Getter
    private String email;
    @Setter
    @Getter
    private String password;

    @Setter
    @Getter
    private boolean enabled = false; // email confirmed

    private String role = "USER";
}
