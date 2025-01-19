package com.yferdin.pigeon_devis_back.security.integration;

import com.yferdin.pigeon_devis_back.security.dto.AuthResponse;
import com.yferdin.pigeon_devis_back.security.dto.LoginRequest;
import com.yferdin.pigeon_devis_back.security.dto.RegisterRequest;
import com.yferdin.pigeon_devis_back.user.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class AuthIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private UserRepository userRepository;

    private static final String VALID_EMAIL = "test@example.com";
    private static final String VALID_PASSWORD = "Test@1234";

    @BeforeEach
    void setup() {
        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setEmail(VALID_EMAIL);
        registerRequest.setPassword(VALID_PASSWORD);
        registerRequest.setFirstName("John");
        registerRequest.setLastName("Doe");
        registerRequest.setPhone("0123456789");

        restTemplate.postForEntity("/api/auth/register", registerRequest, AuthResponse.class);
    }

    @AfterEach
    void cleanup() {
        userRepository.deleteAll();
    }

    @Test
    void login_WithValidCredentials_ShouldReturnToken() {
        LoginRequest request = new LoginRequest();
        request.setEmail(VALID_EMAIL);
        request.setPassword(VALID_PASSWORD);

        ResponseEntity<AuthResponse> response = restTemplate.postForEntity(
            "/api/auth/login",
            request,
            AuthResponse.class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertNotNull(response.getBody().getToken());
        assertEquals(VALID_EMAIL, response.getBody().getEmail());
    }

    @Test
    void login_WithInvalidEmail_ShouldReturnUnauthorized() {
        LoginRequest request = new LoginRequest();
        request.setEmail("wrong@example.com");
        request.setPassword(VALID_PASSWORD);

        ResponseEntity<String> response = restTemplate.postForEntity(
            "/api/auth/login",
            request,
            String.class
        );

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }

    @Test
    void login_WithInvalidPassword_ShouldReturnUnauthorized() {
        LoginRequest request = new LoginRequest();
        request.setEmail(VALID_EMAIL);
        request.setPassword("WrongPassword@123");

        ResponseEntity<String> response = restTemplate.postForEntity(
            "/api/auth/login",
            request,
            String.class
        );

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }

    @Test
    void login_WithEmptyCredentials_ShouldReturnBadRequest() {
        LoginRequest request = new LoginRequest();
        request.setEmail("");
        request.setPassword("");

        ResponseEntity<String> response = restTemplate.postForEntity(
            "/api/auth/login",
            request,
            String.class
        );

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void register_WithValidData_ShouldCreateUser() {
        RegisterRequest request = new RegisterRequest();
        request.setEmail("new@example.com");
        request.setPassword(VALID_PASSWORD);
        request.setFirstName("John");
        request.setLastName("Doe");
        request.setPhone("0123456789");

        ResponseEntity<AuthResponse> response = restTemplate.postForEntity(
            "/api/auth/register",
            request,
            AuthResponse.class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertNotNull(response.getBody().getToken());
        assertEquals(request.getEmail(), response.getBody().getEmail());
        assertEquals(request.getFirstName(), response.getBody().getFirstName());
        assertEquals(request.getLastName(), response.getBody().getLastName());

        assertTrue(userRepository.existsByEmail(request.getEmail()));
    }

    @Test
    void register_WithWeakPassword_ShouldReturnBadRequest() {
        RegisterRequest request = new RegisterRequest();
        request.setEmail("new@example.com");
        request.setPassword("weak");
        request.setFirstName("John");
        request.setLastName("Doe");
        request.setPhone("0123456789");

        ResponseEntity<String> response = restTemplate.postForEntity(
            "/api/auth/register",
            request,
            String.class
        );

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertFalse(userRepository.existsByEmail(request.getEmail()));
    }

    @Test
    void register_WithExistingEmail_ShouldReturnConflict() {
        RegisterRequest request = new RegisterRequest();
        request.setEmail(VALID_EMAIL);
        request.setPassword(VALID_PASSWORD);
        request.setFirstName("John");
        request.setLastName("Doe");
        request.setPhone("0123456789");

        ResponseEntity<String> response = restTemplate.postForEntity(
            "/api/auth/register",
            request,
            String.class
        );

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
    }

    @Test
    void register_WithInvalidEmail_ShouldReturnBadRequest() {
        RegisterRequest request = new RegisterRequest();
        request.setEmail("invalid-email");
        request.setPassword(VALID_PASSWORD);
        request.setFirstName("John");
        request.setLastName("Doe");
        request.setPhone("0123456789");

        ResponseEntity<String> response = restTemplate.postForEntity(
            "/api/auth/register",
            request,
            String.class
        );

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertFalse(userRepository.existsByEmail(request.getEmail()));
    }
} 