package com.example.event.service;

import java.util.Map;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.event.dto.AuthRequest;
import com.example.event.dto.RegisterRequest;
import com.example.event.entity.User;
import com.example.event.exception.BadRequestException;
import com.example.event.repository.UserRepository;
import com.example.event.security.JwtUtil;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository repo;
    private final PasswordEncoder encoder;
    private final JwtUtil jwt;

    public void register(RegisterRequest r) {

        if (r == null || r.getEmail() == null || r.getPassword() == null || r.getRole() == null) {
            throw new BadRequestException("Invalid registration data");
        }

        if (repo.findByEmail(r.getEmail()).isPresent()) {
            throw new BadRequestException("Email already exists");
        }

        User u = new User();
        u.setName(r.getName());
        u.setEmail(r.getEmail());
        u.setPassword(encoder.encode(r.getPassword()));
        u.setRole(r.getRole());

        repo.save(u);
    }

    public Map<String, String> login(AuthRequest r) {

        if (r == null || r.getEmail() == null || r.getPassword() == null) {
            throw new BadRequestException("Invalid login data");
        }

        User u = repo.findByEmail(r.getEmail())
                .orElseThrow(() -> new BadRequestException("Invalid email or password"));

        if (!encoder.matches(r.getPassword(), u.getPassword())) {
            throw new BadRequestException("Invalid email or password");
        }

        return Map.of(
                "token", jwt.generateToken(u.getEmail(), u.getRole().name()),
                "role", u.getRole().name()
        );
    }
}
