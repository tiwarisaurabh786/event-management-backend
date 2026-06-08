package com.example.event.controller;

import com.example.event.dto.ApiResponse;
import com.example.event.dto.AuthRequest;
import com.example.event.dto.RegisterRequest;
import com.example.event.service.AuthService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ApiResponse<?> register(@RequestBody(required = false) RegisterRequest request) {

        if (request == null ||
            request.getEmail() == null ||
            request.getPassword() == null ||
            request.getRole() == null) {

            return new ApiResponse<>(false, "Invalid registration data", null);
        }

        authService.register(request);
        return new ApiResponse<>(true, "User registered successfully", null);
    }

    @PostMapping("/login")
    public ApiResponse<?> login(@RequestBody(required = false) AuthRequest request) {

        if (request == null ||
            request.getEmail() == null ||
            request.getPassword() == null) {

            return new ApiResponse<>(false, "Invalid login data", null);
        }

        return new ApiResponse<>(
                true,
                "Login successful",
                authService.login(request)
        );
    }
}
