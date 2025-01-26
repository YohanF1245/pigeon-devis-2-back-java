package com.yferdin.pigeon_devis_back.business.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Données de création d'une entreprise")
public class CreateBusinessDTO {

    @Schema(description = "Nom de l'entreprise", example = "Ma Super Entreprise")
    @NotBlank(message = "Le nom est obligatoire")
    private String name;

    @Schema(description = "Numéro SIRET de l'entreprise (14 chiffres)", example = "12345678901234")
    @NotBlank(message = "Le SIRET est obligatoire")
    @Pattern(regexp = "^[0-9]{14}$", message = "Le SIRET doit contenir exactement 14 chiffres")
    private String siret;

    @Schema(description = "Adresse de l'entreprise")
    @Valid
    private CreateAddressDTO address;
} 