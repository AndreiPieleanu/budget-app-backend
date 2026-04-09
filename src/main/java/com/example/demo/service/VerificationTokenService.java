package com.example.demo.service;

import com.example.demo.entity.User;
import com.example.demo.entity.VerificationToken;
import com.example.demo.repository.VerificationTokenRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class VerificationTokenService {

    private final VerificationTokenRepository repo;

    public VerificationTokenService(VerificationTokenRepository repo) {
        this.repo = repo;
    }

    public String createToken(User user) {
        String token = UUID.randomUUID().toString();

        VerificationToken vt = new VerificationToken();
        vt.setToken(token);
        vt.setUser(user);
        vt.setExpiryDate(LocalDateTime.now().plusHours(24));

        repo.save(vt);

        return token;
    }

    public VerificationToken getByToken(String token) {
        return repo.findByToken(token)
                .orElseThrow(() -> new RuntimeException("Invalid token"));
    }
}
