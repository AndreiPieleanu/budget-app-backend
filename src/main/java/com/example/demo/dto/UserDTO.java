package com.example.demo.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class UserDTO implements Serializable {
    private Integer id;
    private String email;
    private String password;
    private boolean enabled = false;
    private String role = "USER";
}
