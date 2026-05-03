package com.example.demo.response;

import lombok.Data;

@Data
public class UserResponse {
    private Integer id;
    private String email;
    private boolean enabled = false;
    private String role = "USER";
}
