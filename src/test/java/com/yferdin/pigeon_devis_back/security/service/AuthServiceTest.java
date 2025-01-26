package com.yferdin.pigeon_devis_back.security.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.yferdin.pigeon_devis_back.security.dto.AuthResponse;
import com.yferdin.pigeon_devis_back.security.dto.LoginRequest;
import com.yferdin.pigeon_devis_back.security.dto.RegisterRequest;
import com.yferdin.pigeon_devis_back.security.exception.EmailAlreadyExistsException;
import com.yferdin.pigeon_devis_back.security.jwt.JwtTokenProvider;
import com.yferdin.pigeon_devis_back.security.model.VerificationToken;
import com.yferdin.pigeon_devis_back.security.repository.VerificationTokenRepository;
import com.yferdin.pigeon_devis_back.user.model.User;
import com.yferdin.pigeon_devis_back.user.model.RoleType;
import com.yferdin.pigeon_devis_back.user.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    private JwtTokenProvider tokenProvider;
    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private VerificationTokenRepository verificationTokenRepository;
    @Mock
    private EmailService emailService;

    @InjectMocks
    private AuthService authService;

    private RegisterRequest registerRequest;
    private LoginRequest loginRequest;
    private User user;

    @BeforeEach
    void setUp() {
        registerRequest = new RegisterRequest();
        registerRequest.setEmail("test@test.com");
        registerRequest.setPassword("password");
        registerRequest.setFirstName("Test");
        registerRequest.setLastName("User");

        loginRequest = new LoginRequest();
        loginRequest.setEmail("test@test.com");
        loginRequest.setPassword("password");

        user = User.builder()
                .email("test@test.com")
                .password("encodedPassword")
                .firstName("Test")
                .lastName("User")
                .role(RoleType.ROLE_USER)
                .enabled(true)
                .verified(false)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        lenient().when(passwordEncoder.encode(any())).thenReturn("encodedPassword");
        lenient().when(userRepository.save(any(User.class))).thenReturn(user);
    }

    @Test
    void register_WithValidRequest_ShouldCreateUser() {
        // When
        authService.register(registerRequest);

        // Then
        verify(userRepository).save(any(User.class));
        verify(verificationTokenRepository).save(any(VerificationToken.class));
        verify(emailService).sendVerificationEmail(anyString(), anyString());
    }

    @Test
    void register_WithExistingEmail_ShouldThrowException() {
        // Given
        when(userRepository.existsByEmail(anyString())).thenReturn(true);

        // When & Then
        assertThrows(EmailAlreadyExistsException.class, () -> {
            authService.register(registerRequest);
        });
    }

    @Test
    void login_ShouldReturnAuthResponse() {
        // Given
        Authentication mockAuth = mock(Authentication.class);
        when(mockAuth.getPrincipal()).thenReturn(user);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
            .thenReturn(mockAuth);
        when(tokenProvider.generateToken(any(User.class))).thenReturn("jwt-token");

        // When
        AuthResponse response = authService.login(loginRequest);

        // Then
        assertNotNull(response);
        assertEquals("jwt-token", response.getToken());
        assertEquals(loginRequest.getEmail(), response.getEmail());
        assertEquals(user.getFirstName(), response.getFirstName());
        assertEquals(user.getLastName(), response.getLastName());
    }
} 