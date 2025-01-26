package com.yferdin.pigeon_devis_back.business.dto;

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
public class CreateBusinessDTO {

    @NotBlank(message = "Le nom est obligatoire")
    private String name;

    @NotBlank(message = "Le SIRET est obligatoire")
    @Pattern(regexp = "^[0-9]{14}$", message = "Le SIRET doit contenir exactement 14 chiffres")
    private String siret;

    @Valid
    private CreateAddressDTO address;
} 