package com.yferdin.pigeon_devis_back.business.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yferdin.pigeon_devis_back.business.dto.CreateAddressDTO;
import com.yferdin.pigeon_devis_back.business.dto.CreateBusinessDTO;
import com.yferdin.pigeon_devis_back.business.repository.BusinessRepository;
import com.yferdin.pigeon_devis_back.security.dto.LoginRequest;
import com.yferdin.pigeon_devis_back.security.dto.RegisterRequest;
import com.yferdin.pigeon_devis_back.user.repository.AddressRepository;
import com.yferdin.pigeon_devis_back.user.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class BusinessIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private BusinessRepository businessRepository;

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private UserRepository userRepository;

    private String authToken;
    private CreateBusinessDTO createBusinessDTO;

    @BeforeEach
    void setUp() throws Exception {
        // Nettoyer la base de données
        businessRepository.deleteAll();
        addressRepository.deleteAll();
        userRepository.deleteAll();

        // Créer un utilisateur de test
        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setEmail("test@test.com");
        registerRequest.setPassword("Password123!");
        registerRequest.setFirstName("Test");
        registerRequest.setLastName("User");

        mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registerRequest)))
                .andExpect(status().isOk());

        // Se connecter pour obtenir le token
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("test@test.com");
        loginRequest.setPassword("Password123!");

        MvcResult result = mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andReturn();

        String response = result.getResponse().getContentAsString();
        authToken = objectMapper.readTree(response).get("token").asText();

        // Préparer les données de test pour l'entreprise
        createBusinessDTO = new CreateBusinessDTO();
        createBusinessDTO.setSiret("12345678901234");
        createBusinessDTO.setApeCode("6201Z");
        createBusinessDTO.setTaxCode("FR12345678901");

        CreateAddressDTO addressDTO = new CreateAddressDTO();
        addressDTO.setStreetNumber("1");
        addressDTO.setStreetName("Rue de Test");
        addressDTO.setZipCode("75000");
        addressDTO.setCity("Paris");
        createBusinessDTO.setAddress(addressDTO);
    }

    @AfterEach
    void tearDown() {
        businessRepository.deleteAll();
        addressRepository.deleteAll();
    }

    @Test
    void createBusiness_WithValidData_ShouldCreateBusinessAndAddress() throws Exception {
        // When
        mockMvc.perform(post("/api/businesses")
                .header("Authorization", "Bearer " + authToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createBusinessDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.siret").value(createBusinessDTO.getSiret()))
                .andExpect(jsonPath("$.apeCode").value(createBusinessDTO.getApeCode()))
                .andExpect(jsonPath("$.taxCode").value(createBusinessDTO.getTaxCode()));

        // Then
        assertTrue(businessRepository.existsBySiret(createBusinessDTO.getSiret()));
    }

    @Test
    void createBusiness_WithExistingSiret_ShouldReturnConflict() throws Exception {
        // Given
        mockMvc.perform(post("/api/businesses")
                .header("Authorization", "Bearer " + authToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createBusinessDTO)))
                .andExpect(status().isCreated());

        // When & Then
        mockMvc.perform(post("/api/businesses")
                .header("Authorization", "Bearer " + authToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createBusinessDTO)))
                .andExpect(status().isConflict());
    }

    @Test
    void createBusiness_WithInvalidSiret_ShouldReturnBadRequest() throws Exception {
        // Given
        createBusinessDTO.setSiret("123"); // SIRET invalide

        // When & Then
        mockMvc.perform(post("/api/businesses")
                .header("Authorization", "Bearer " + authToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createBusinessDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createBusiness_WithoutAuthentication_ShouldReturnUnauthorized() throws Exception {
        // When & Then
        mockMvc.perform(post("/api/businesses")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createBusinessDTO)))
                .andExpect(status().isUnauthorized());
    }
} 