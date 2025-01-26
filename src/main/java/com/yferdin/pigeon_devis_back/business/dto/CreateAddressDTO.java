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

    @NotBlank(message = "La rue est obligatoire")
    @Size(max = 255, message = "La rue ne peut pas dépasser 255 caractères")
    private String street;

    @NotBlank(message = "La ville est obligatoire")
    @Size(max = 255, message = "La ville ne peut pas dépasser 255 caractères")
    private String city;

    @NotBlank(message = "Le code postal est obligatoire")
    @Size(max = 255, message = "Le code postal ne peut pas dépasser 255 caractères")
    private String zipCode;

    @NotBlank(message = "Le pays est obligatoire")
    @Size(max = 255, message = "Le pays ne peut pas dépasser 255 caractères")
    private String country;
} 