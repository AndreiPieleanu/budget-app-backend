package com.example.demo.controllers;

import com.example.demo.dto.AuthRequest;
import com.example.demo.dto.AuthResponse;
import com.example.demo.dto.RegisterRequest;
import com.example.demo.dto.UserDTO;
import com.example.demo.entity.User;
import com.example.demo.entity.VerificationToken;
import com.example.demo.service.EmailService;
import com.example.demo.service.JwtService;
import com.example.demo.service.UserService;
import com.example.demo.service.VerificationTokenService;
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
                "http://localhost:3000/confirm?token=" + token
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

        String token = jwtService.generate(user.getEmail());

        return new AuthResponse(token);
    }
}
