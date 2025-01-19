package com.yferdin.pigeon_devis_back.security.integration;

import com.yferdin.pigeon_devis_back.security.dto.AuthResponse;
import com.yferdin.pigeon_devis_back.security.dto.RegisterRequest;
import com.yferdin.pigeon_devis_back.user.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
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

    @AfterEach
    void cleanup() {
        userRepository.deleteAll();
    }

    @Test
    void register_WithValidData_ShouldCreateUser() {
        RegisterRequest request = new RegisterRequest();
        request.setEmail("test@example.com");
        request.setPassword("Test@1234");
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
        request.setEmail("test@example.com");
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
        request.setEmail("test@example.com");
        request.setPassword("Test@1234");
        request.setFirstName("John");
        request.setLastName("Doe");
        request.setPhone("0123456789");

        restTemplate.postForEntity("/api/auth/register", request, AuthResponse.class);

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
        request.setPassword("Test@1234");
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