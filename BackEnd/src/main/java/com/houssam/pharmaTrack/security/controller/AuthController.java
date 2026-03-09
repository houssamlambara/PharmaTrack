package com.houssam.pharmaTrack.security.controller;

import com.houssam.pharmaTrack.response.ApiResponse;
import com.houssam.pharmaTrack.security.dto.AuthRequest;
import com.houssam.pharmaTrack.security.dto.AuthResponse;
import com.houssam.pharmaTrack.security.dto.RegisterRequest;
import com.houssam.pharmaTrack.security.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.houssam.pharmaTrack.dto.responseDTO.UserResponseDTO;
import java.util.List;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody AuthRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }

    @PostMapping("/register")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<AuthResponse>> register(@Valid @RequestBody RegisterRequest request) {
        AuthResponse response = authService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>("Inscription réussie", response)
        );
    }

    @GetMapping("/users")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<List<UserResponseDTO>>> getAllUsers() {
        return ResponseEntity.ok(new ApiResponse<>("Liste des utilisateurs", authService.getAllUsers()));
    }
}
