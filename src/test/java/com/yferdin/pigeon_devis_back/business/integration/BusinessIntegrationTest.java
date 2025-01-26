package com.yferdin.pigeon_devis_back.business.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yferdin.pigeon_devis_back.business.dto.CreateAddressDTO;
import com.yferdin.pigeon_devis_back.business.dto.CreateBusinessDTO;
import com.yferdin.pigeon_devis_back.business.repository.BusinessRepository;
import com.yferdin.pigeon_devis_back.security.dto.LoginRequest;
import com.yferdin.pigeon_devis_back.security.dto.RegisterRequest;
import com.yferdin.pigeon_devis_back.security.repository.VerificationTokenRepository;
import com.yferdin.pigeon_devis_back.security.model.VerificationToken;
import com.yferdin.pigeon_devis_back.user.repository.AddressRepository;
import com.yferdin.pigeon_devis_back.user.repository.UserRepository;
import com.yferdin.pigeon_devis_back.user.model.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;
import javax.sql.DataSource;
import org.springframework.jdbc.core.JdbcTemplate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
@Sql(scripts = "classpath:schema.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
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

    @Autowired
    private VerificationTokenRepository verificationTokenRepository;

    @Autowired
    private DataSource dataSource;

    private JdbcTemplate jdbcTemplate;
    private String authToken;
    private CreateBusinessDTO createBusinessDTO;

    @BeforeEach
    void setUp() throws Exception {
        jdbcTemplate = new JdbcTemplate(dataSource);
        
        // Nettoyer la base de données dans l'ordre correct
        jdbcTemplate.execute("SET REFERENTIAL_INTEGRITY FALSE");
        jdbcTemplate.execute("TRUNCATE TABLE businesses");
        jdbcTemplate.execute("TRUNCATE TABLE addresses");
        jdbcTemplate.execute("TRUNCATE TABLE verification_tokens");
        jdbcTemplate.execute("TRUNCATE TABLE users");
        jdbcTemplate.execute("SET REFERENTIAL_INTEGRITY TRUE");

        // Créer un utilisateur de test
        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setEmail("test@test.com");
        registerRequest.setPassword("Test123!");
        registerRequest.setFirstName("John");
        registerRequest.setLastName("Doe");
        registerRequest.setPhone("+33612345678");

        MvcResult registerResult = mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registerRequest)))
                .andExpect(status().isOk())
                .andReturn();

        // Vérifier l'utilisateur
        User user = userRepository.findByEmail("test@test.com").orElseThrow();
        VerificationToken token = verificationTokenRepository.findByUser(user).orElseThrow();
        mockMvc.perform(get("/api/auth/verify")
                .param("token", token.getToken()))
                .andExpect(status().isOk());

        // Se connecter pour obtenir le token
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("test@test.com");
        loginRequest.setPassword("Test123!");

        MvcResult result = mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andReturn();

        String response = result.getResponse().getContentAsString();
        authToken = objectMapper.readTree(response).get("data").get("token").asText();

        // Préparer les données de test pour l'entreprise
        createBusinessDTO = new CreateBusinessDTO();
        createBusinessDTO.setName("Test Business");
        createBusinessDTO.setSiret("12345678901234");

        CreateAddressDTO addressDTO = new CreateAddressDTO();
        addressDTO.setStreet("1 Rue de Test");
        addressDTO.setCity("Paris");
        addressDTO.setZipCode("75000");
        addressDTO.setCountry("France");
        createBusinessDTO.setAddress(addressDTO);
    }

    @AfterEach
    void tearDown() {
        jdbcTemplate.execute("SET REFERENTIAL_INTEGRITY FALSE");
        jdbcTemplate.execute("TRUNCATE TABLE businesses");
        jdbcTemplate.execute("TRUNCATE TABLE addresses");
        jdbcTemplate.execute("TRUNCATE TABLE verification_tokens");
        jdbcTemplate.execute("TRUNCATE TABLE users");
        jdbcTemplate.execute("SET REFERENTIAL_INTEGRITY TRUE");
    }

    @Test
    void createBusiness_WithValidData_ShouldCreateBusinessAndAddress() throws Exception {
        // When
        mockMvc.perform(post("/api/businesses")
                .header("Authorization", "Bearer " + authToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createBusinessDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value(createBusinessDTO.getName()))
                .andExpect(jsonPath("$.siret").value(createBusinessDTO.getSiret()));

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