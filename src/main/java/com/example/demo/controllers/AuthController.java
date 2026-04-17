package com.example.demo.controllers;

import com.example.demo.dto.*;
import com.example.demo.entity.User;
import com.example.demo.entity.VerificationToken;
import com.example.demo.service.EmailService;
import com.example.demo.service.JwtService;
import com.example.demo.service.UserService;
import com.example.demo.service.VerificationTokenService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserService userService;
    private final VerificationTokenService tokenService;
    private final EmailService emailService;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    @Value("${FRONTEND_URL}")
    private String frontendUrl;

    public AuthController(UserService userService,
                          VerificationTokenService tokenService,
                          EmailService emailService,
                          JwtService jwtService,
                          PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.tokenService = tokenService;
        this.emailService = emailService;
        this.jwtService = jwtService;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/register")
    public String register(@RequestBody AuthRequest req) {
        User user = userService.register(req.getEmail(), req.getPassword());

        String token = tokenService.createToken(user);

        emailService.send(
                user.getEmail(),
                "Confirm your account",
                frontendUrl + "/confirm?token=" + token
        );

        return "User registered. Check email.";
    }

    @GetMapping("/confirm")
    public String confirm(@RequestParam String token) {
        VerificationToken vt = tokenService.getByToken(token);

        userService.enableUser(vt.getUser());

        return "Account confirmed";
    }

    @PostMapping("/login")
    public AuthResponse login(@RequestBody AuthRequest req) {
        UserDTO user = userService.findByEmail(req.getEmail());

        if(user == null){
            throw new RuntimeException("User does not exist!");
        }

        if (!passwordEncoder.matches(req.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }

        if (!user.isEnabled()) {
            throw new RuntimeException("Email not confirmed");
        }

        String token = jwtService.generate(user);

        return new AuthResponse(token);
    }
    @PostMapping("/me")
    public ResponseEntity<Integer> getUserIdByToken(@RequestBody UserRequest userRequest){
        return ResponseEntity.ok(jwtService.extractUserId(userRequest));
    }
}
