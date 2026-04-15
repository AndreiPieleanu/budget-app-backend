package com.example.demo.service;

import com.example.demo.dto.UserDTO;
import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @CacheEvict(value = "users")
    public User register(String email, String password) {
        User user = new User();
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        user.setEnabled(false);

        return userRepository.save(user);
    }
    @Cacheable(value = "users", key = "#email")
    public UserDTO findByEmail(String email) {
        Optional<User> userOptional = userRepository.findByEmail(email);
        if(userOptional.isEmpty()) return null;
        UserDTO dto = new UserDTO();
        userOptional.ifPresent(u -> {
            dto.setId(u.getId());
            dto.setEmail(u.getEmail());
            dto.setRole(u.getRole());
            dto.setEnabled(u.isEnabled());
            dto.setPassword(u.getPassword());
        });
        return dto;
    }

    @CacheEvict(value = "users", allEntries = true)
    public void enableUser(User user) {
        user.setEnabled(true);
        userRepository.save(user);
    }
}
