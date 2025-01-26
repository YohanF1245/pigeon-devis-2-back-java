package com.yferdin.pigeon_devis_back.security.controller;

import com.yferdin.pigeon_devis_back.security.dto.AuthResponse;
import com.yferdin.pigeon_devis_back.security.dto.LoginRequest;
import com.yferdin.pigeon_devis_back.security.dto.RegisterRequest;
import com.yferdin.pigeon_devis_back.security.exception.TokenExpiredException;
import com.yferdin.pigeon_devis_back.security.exception.TokenNotFoundException;
import com.yferdin.pigeon_devis_back.security.service.AuthService;
import com.yferdin.pigeon_devis_back.shared.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Authentification", description = "API d'authentification")
public class AuthController {

    private final AuthService authService;

    @Operation(summary = "Connexion utilisateur", description = "Permet à un utilisateur de se connecter avec son email et son mot de passe")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Connexion réussie",
            content = @Content(schema = @Schema(implementation = AuthResponse.class))),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Identifiants invalides")
    })
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

    @Operation(summary = "Inscription utilisateur", description = "Permet à un utilisateur de créer un compte")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "Inscription réussie",
            content = @Content(schema = @Schema(implementation = AuthResponse.class))),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Données d'inscription invalides")
    })
    @PostMapping("/register")
    public ResponseEntity<ApiResponse<AuthResponse>> register(@RequestBody RegisterRequest registerRequest) {
        try {
            AuthResponse response = authService.register(registerRequest);
            return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>("SUCCESS", 
                "Inscription réussie. Veuillez vérifier votre email pour activer votre compte", response));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(new ApiResponse<>("ERROR", e.getMessage(), null));
        }
    }

    @Operation(summary = "Vérification du compte", description = "Permet de vérifier le compte d'un utilisateur via le token reçu par email")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Compte vérifié avec succès"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Token invalide ou expiré")
    })
    @GetMapping("/verify")
    public ResponseEntity<ApiResponse<Void>> verifyAccount(
        @Parameter(description = "Token de vérification reçu par email") @RequestParam String token
    ) {
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