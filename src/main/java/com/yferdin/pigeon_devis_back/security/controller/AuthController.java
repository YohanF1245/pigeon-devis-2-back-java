package com.yferdin.pigeon_devis_back.security.controller;

import com.yferdin.pigeon_devis_back.security.dto.AuthResponse;
import com.yferdin.pigeon_devis_back.security.dto.LoginRequest;
import com.yferdin.pigeon_devis_back.security.dto.RegisterRequest;
import com.yferdin.pigeon_devis_back.security.exception.TokenExpiredException;
import com.yferdin.pigeon_devis_back.security.exception.TokenNotFoundException;
import com.yferdin.pigeon_devis_back.security.service.AuthService;
import com.yferdin.pigeon_devis_back.shared.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthResponse>> login(@RequestBody LoginRequest loginRequest) {
        try {
            AuthResponse response = authService.login(loginRequest);
            return ResponseEntity.ok(new ApiResponse<>("SUCCESS", "Connexion réussie", response));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(new ApiResponse<>("ERROR", e.getMessage(), null));
        }
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<AuthResponse>> register(@RequestBody RegisterRequest registerRequest) {
        try {
            AuthResponse response = authService.register(registerRequest);
            return ResponseEntity.ok(new ApiResponse<>("SUCCESS", 
                "Inscription réussie. Veuillez vérifier votre email pour activer votre compte", response));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(new ApiResponse<>("ERROR", e.getMessage(), null));
        }
    }

    @GetMapping("/verify")
    public ResponseEntity<ApiResponse<Void>> verifyAccount(@RequestParam String token) {
        try {
            authService.verifyAccount(token);
            return ResponseEntity.ok(new ApiResponse<>("SUCCESS", 
                "Compte vérifié avec succès. Vous pouvez maintenant vous connecter", null));
        } catch (TokenNotFoundException e) {
            return ResponseEntity.badRequest()
                .body(new ApiResponse<>("ERROR", "Token de vérification invalide", null));
        } catch (TokenExpiredException e) {
            return ResponseEntity.badRequest()
                .body(new ApiResponse<>("ERROR", "Le token de vérification a expiré", null));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(new ApiResponse<>("ERROR", e.getMessage(), null));
        }
    }
} 