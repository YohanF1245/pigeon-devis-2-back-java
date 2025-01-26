package com.yferdin.pigeon_devis_back.business.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Données de création d'une adresse")
public class CreateAddressDTO {

    @Schema(description = "Rue de l'adresse", example = "123 rue de la Paix")
    @NotBlank(message = "La rue est obligatoire")
    @Size(max = 255, message = "La rue ne peut pas dépasser 255 caractères")
    private String street;

    @Schema(description = "Ville de l'adresse", example = "Paris")
    @NotBlank(message = "La ville est obligatoire")
    @Size(max = 255, message = "La ville ne peut pas dépasser 255 caractères")
    private String city;

    @Schema(description = "Code postal de l'adresse", example = "75000")
    @NotBlank(message = "Le code postal est obligatoire")
    @Size(max = 255, message = "Le code postal ne peut pas dépasser 255 caractères")
    private String zipCode;

    @Schema(description = "Pays de l'adresse", example = "France")
    @NotBlank(message = "Le pays est obligatoire")
    @Size(max = 255, message = "Le pays ne peut pas dépasser 255 caractères")
    private String country;
} 