package com.yferdin.pigeon_devis_back.security.dto;

import com.yferdin.pigeon_devis_back.security.validation.StrongPassword;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "Données d'inscription")
public class RegisterRequest {

    @Schema(description = "Email de l'utilisateur", example = "user@example.com")
    @NotBlank(message = "L'email est obligatoire")
    @Email(message = "L'email doit être valide")
    private String email;

    @Schema(description = "Mot de passe de l'utilisateur (8 caractères minimum, 1 majuscule, 1 minuscule, 1 chiffre, 1 caractère spécial)", example = "P@ssw0rd")
    @NotBlank(message = "Le mot de passe est obligatoire")
    @StrongPassword
    private String password;

    @Schema(description = "Prénom de l'utilisateur", example = "John")
    @NotBlank(message = "Le prénom est obligatoire")
    @Size(min = 2, max = 50, message = "Le prénom doit contenir entre 2 et 50 caractères")
    private String firstName;

    @Schema(description = "Nom de l'utilisateur", example = "Doe")
    @NotBlank(message = "Le nom est obligatoire")
    @Size(min = 2, max = 50, message = "Le nom doit contenir entre 2 et 50 caractères")
    private String lastName;

    @Schema(description = "Numéro de téléphone au format français", example = "+33612345678")
    @Size(max = 15, message = "Le numéro de téléphone ne doit pas dépasser 15 caractères")
    @Pattern(regexp = "^(?:(?:\\+|00)33|0)\\s*[1-9](?:[\\s.-]*\\d{2}){4}$", message = "Le numéro de téléphone doit être au format français")
    private String phone;
} 