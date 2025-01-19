package com.yferdin.pigeon_devis_back.business.dto;

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
public class CreateAddressDTO {

    @NotBlank(message = "Le numéro de rue est obligatoire")
    @Size(max = 10, message = "Le numéro de rue ne peut pas dépasser 10 caractères")
    private String streetNumber;

    @NotBlank(message = "Le nom de rue est obligatoire")
    @Size(max = 255, message = "Le nom de rue ne peut pas dépasser 255 caractères")
    private String streetName;

    @NotBlank(message = "Le code postal est obligatoire")
    @Size(max = 10, message = "Le code postal ne peut pas dépasser 10 caractères")
    private String zipCode;

    @NotBlank(message = "La ville est obligatoire")
    @Size(max = 100, message = "La ville ne peut pas dépasser 100 caractères")
    private String city;

    @Size(max = 255, message = "Le complément d'adresse ne peut pas dépasser 255 caractères")
    private String complement;
} 