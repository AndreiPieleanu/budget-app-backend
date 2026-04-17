package com.example.demo.dto;

import lombok.Data;

@Data
public class UserResponse {
    private Integer id;
    private String email;
    private boolean enabled = false;
    private String role = "USER";
}
