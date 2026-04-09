package com.example.demo.config;

import io.jsonwebtoken.security.Keys;

import java.util.Base64;

public class KeyGen {
    public static void main(String[] args) {
        var key = Keys.secretKeyFor(io.jsonwebtoken.SignatureAlgorithm.HS256);
        String base64 = Base64.getEncoder().encodeToString(key.getEncoded());
        System.out.println(base64);
    }
}
