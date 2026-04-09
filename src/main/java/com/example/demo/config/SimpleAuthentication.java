package com.example.demo.config;

import org.springframework.security.authentication.AbstractAuthenticationToken;

import java.util.Collections;

public class SimpleAuthentication extends AbstractAuthenticationToken {

    private final String email;

    public SimpleAuthentication(String email) {
        super(Collections.emptyList());
        this.email = email;
        setAuthenticated(true);
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return email;
    }
}