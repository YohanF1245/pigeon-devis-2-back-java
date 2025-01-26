package com.yferdin.pigeon_devis_back.business.controller;

import com.yferdin.pigeon_devis_back.business.dto.CreateAddressDTO;
import com.yferdin.pigeon_devis_back.business.dto.CreateBusinessDTO;
import com.yferdin.pigeon_devis_back.business.exception.SiretAlreadyExistsException;
import com.yferdin.pigeon_devis_back.business.model.Business;
import com.yferdin.pigeon_devis_back.business.service.BusinessService;
import com.yferdin.pigeon_devis_back.security.service.AuthService;
import com.yferdin.pigeon_devis_back.user.model.User;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class BusinessControllerTest {

    @Mock
    private BusinessService businessService;

    @Mock
    private AuthService authService;

    @Mock
    private UserDetails userDetails;

    @InjectMocks
    private BusinessController businessController;

    private CreateBusinessDTO createBusinessDTO;
    private User user;
    private Business business;
    private Validator validator;

    @BeforeEach
    void setUp() {
        // Préparer les données de test
        createBusinessDTO = new CreateBusinessDTO();
        createBusinessDTO.setName("Test Business");
        createBusinessDTO.setSiret("12345678901234");

        CreateAddressDTO addressDTO = new CreateAddressDTO();
        addressDTO.setStreet("1 Rue de Test");
        addressDTO.setCity("Paris");
        addressDTO.setZipCode("75000");
        addressDTO.setCountry("France");
        createBusinessDTO.setAddress(addressDTO);

        user = new User();
        user.setEmail("test@test.com");

        business = new Business();
        business.setName(createBusinessDTO.getName());
        business.setSiret(createBusinessDTO.getSiret());

        // Initialiser le validateur
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void createBusiness_WithValidData_ShouldReturnCreatedBusiness() {
        // Given
        when(authService.getUserFromUserDetails(userDetails)).thenReturn(user);
        when(businessService.createBusiness(any(CreateBusinessDTO.class), any(User.class))).thenReturn(business);

        // When
        ResponseEntity<Business> response = businessController.createBusiness(createBusinessDTO, userDetails);

        // Then
        assertNotNull(response);
        assertEquals(201, response.getStatusCodeValue());
        assertEquals(business, response.getBody());
    }

    @Test
    void createBusiness_WithExistingSiret_ShouldThrowException() {
        // Given
        when(authService.getUserFromUserDetails(userDetails)).thenReturn(user);
        when(businessService.createBusiness(any(CreateBusinessDTO.class), any(User.class)))
                .thenThrow(new SiretAlreadyExistsException("SIRET existe déjà"));

        // When & Then
        assertThrows(SiretAlreadyExistsException.class, () -> 
            businessController.createBusiness(createBusinessDTO, userDetails)
        );
    }

    @Test
    void createBusiness_WithInvalidSiret_ShouldFailValidation() {
        // Given
        createBusinessDTO.setSiret("123"); // SIRET invalide

        // When
        Set<ConstraintViolation<CreateBusinessDTO>> violations = validator.validate(createBusinessDTO);

        // Then
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
                .anyMatch(violation -> violation.getPropertyPath().toString().equals("siret")));
    }

    @Test
    void createBusiness_WithInvalidAddress_ShouldFailValidation() {
        // Given
        CreateAddressDTO invalidAddress = new CreateAddressDTO();
        // Ne pas définir les champs obligatoires
        createBusinessDTO.setAddress(invalidAddress);

        // When
        Set<ConstraintViolation<CreateBusinessDTO>> violations = validator.validate(createBusinessDTO);

        // Then
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
                .anyMatch(violation -> violation.getPropertyPath().toString().contains("address")));
    }

    @Test
    void createBusiness_WithInvalidUser_ShouldThrowException() {
        // Given
        when(authService.getUserFromUserDetails(userDetails))
                .thenThrow(new UsernameNotFoundException("Utilisateur non trouvé"));

        // When & Then
        assertThrows(UsernameNotFoundException.class, () ->
            businessController.createBusiness(createBusinessDTO, userDetails)
        );
    }
} 