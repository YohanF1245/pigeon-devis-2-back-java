package com.yferdin.pigeon_devis_back.security.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "Données de connexion")
public class LoginRequest {

    @Schema(description = "Email de l'utilisateur", example = "user@example.com")
    @NotBlank(message = "L'email est obligatoire")
    @Email(message = "L'email doit être valide")
    private String email;

    @Schema(description = "Mot de passe de l'utilisateur", example = "P@ssw0rd")
    @NotBlank(message = "Le mot de passe est obligatoire")
    private String password;
} 