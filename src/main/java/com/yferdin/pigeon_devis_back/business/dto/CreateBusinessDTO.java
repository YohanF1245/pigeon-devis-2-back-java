package com.yferdin.pigeon_devis_back.business.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateBusinessDTO {

    @NotBlank(message = "Le SIRET est obligatoire")
    @Pattern(regexp = "^[0-9]{14}$", message = "Le SIRET doit contenir exactement 14 chiffres")
    private String siret;

    @NotBlank(message = "Le code APE est obligatoire")
    @Pattern(regexp = "^[0-9]{4}[A-Z]$", message = "Le code APE doit être au format 9999X")
    private String apeCode;

    @NotBlank(message = "Le code fiscal est obligatoire")
    @Size(max = 50, message = "Le code fiscal ne peut pas dépasser 50 caractères")
    private String taxCode;

    @Valid
    private CreateAddressDTO address;
} 