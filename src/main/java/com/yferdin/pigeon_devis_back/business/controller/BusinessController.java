package com.yferdin.pigeon_devis_back.business.controller;

import com.yferdin.pigeon_devis_back.business.dto.CreateBusinessDTO;
import com.yferdin.pigeon_devis_back.business.model.Business;
import com.yferdin.pigeon_devis_back.business.service.BusinessService;
import com.yferdin.pigeon_devis_back.security.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/businesses")
@RequiredArgsConstructor
public class BusinessController {

    private final BusinessService businessService;
    private final AuthService authService;

    @PostMapping
    public ResponseEntity<Business> createBusiness(
            @Valid @RequestBody CreateBusinessDTO createBusinessDTO,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        var user = authService.getUserFromUserDetails(userDetails);
        var business = businessService.createBusiness(createBusinessDTO, user);
        return ResponseEntity.created(null).body(business);
    }
} 