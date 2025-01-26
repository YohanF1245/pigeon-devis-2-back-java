package com.yferdin.pigeon_devis_back.business.controller;

import com.yferdin.pigeon_devis_back.business.dto.CreateBusinessDTO;
import com.yferdin.pigeon_devis_back.business.model.Business;
import com.yferdin.pigeon_devis_back.business.service.BusinessService;
import com.yferdin.pigeon_devis_back.security.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.context.request.RequestContextHolder;

import java.net.URI;

@RestController
@RequestMapping("/api/businesses")
@RequiredArgsConstructor
@Tag(name = "Entreprises", description = "API de gestion des entreprises")
@SecurityRequirement(name = "bearerAuth")
public class BusinessController {

    private final BusinessService businessService;
    private final AuthService authService;

    @Operation(summary = "Créer une entreprise", description = "Permet à un utilisateur authentifié de créer une entreprise")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Entreprise créée avec succès",
            content = @Content(schema = @Schema(implementation = Business.class))),
        @ApiResponse(responseCode = "400", description = "Données invalides"),
        @ApiResponse(responseCode = "401", description = "Non authentifié"),
        @ApiResponse(responseCode = "409", description = "SIRET déjà utilisé")
    })
    @PostMapping
    public ResponseEntity<Business> createBusiness(
            @Parameter(description = "Données de l'entreprise à créer") @Valid @RequestBody CreateBusinessDTO createBusinessDTO,
            @Parameter(hidden = true) @AuthenticationPrincipal UserDetails userDetails
    ) {
        var user = authService.getUserFromUserDetails(userDetails);
        var business = businessService.createBusiness(createBusinessDTO, user);
        URI location;
        if (RequestContextHolder.getRequestAttributes() != null) {
            location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(business.getId())
                .toUri();
        } else {
            location = URI.create("/api/businesses/" + business.getId());
        }
        return ResponseEntity.created(location).body(business);
    }
} 