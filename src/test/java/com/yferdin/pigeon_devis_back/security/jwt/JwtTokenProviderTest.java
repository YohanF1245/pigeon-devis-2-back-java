package com.yferdin.pigeon_devis_back.security.jwt;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;

import com.yferdin.pigeon_devis_back.security.config.JwtConfig;

@ExtendWith(MockitoExtension.class)
class JwtTokenProviderTest {

    @Mock
    private JwtConfig jwtConfig;

    private JwtTokenProvider tokenProvider;
    private static final String SECRET = "testSecretKey123456789012345678901234567890";
    private static final long EXPIRATION = 3600000; // 1 heure

    @BeforeEach
    void setUp() {
        lenient().when(jwtConfig.getSecret()).thenReturn(SECRET);
        lenient().when(jwtConfig.getExpiration()).thenReturn(EXPIRATION);
        tokenProvider = new JwtTokenProvider(jwtConfig);
    }

    @Test
    void createToken_ShouldGenerateValidToken() {
        // Given
        Authentication authentication = new UsernamePasswordAuthenticationToken(
            "test@test.com", 
            null, 
            AuthorityUtils.createAuthorityList("ROLE_USER")
        );

        // When
        String token = tokenProvider.createToken(authentication);

        // Then
        assertNotNull(token);
        assertTrue(token.split("\\.").length == 3);
        assertTrue(tokenProvider.validateToken(token));
    }

    @Test
    void validateToken_WithValidToken_ShouldReturnTrue() {
        // Given
        Authentication authentication = new UsernamePasswordAuthenticationToken(
            "test@test.com", 
            null, 
            AuthorityUtils.createAuthorityList("ROLE_USER")
        );
        String token = tokenProvider.createToken(authentication);

        // When & Then
        assertTrue(tokenProvider.validateToken(token));
    }

    @Test
    void validateToken_WithInvalidToken_ShouldReturnFalse() {
        // Given
        String invalidToken = "invalid.token.here";

        // When & Then
        assertFalse(tokenProvider.validateToken(invalidToken));
    }

    @Test
    void getAuthentication_ShouldReturnValidAuthentication() {
        // Given
        Authentication originalAuth = new UsernamePasswordAuthenticationToken(
            "test@test.com", 
            null, 
            AuthorityUtils.createAuthorityList("ROLE_USER")
        );
        String token = tokenProvider.createToken(originalAuth);

        // When
        Authentication resultAuth = tokenProvider.getAuthentication(token);

        // Then
        assertNotNull(resultAuth);
        assertEquals("test@test.com", resultAuth.getName());
        assertTrue(resultAuth.getAuthorities().stream()
            .anyMatch(a -> a.getAuthority().equals("ROLE_USER")));
    }
} 