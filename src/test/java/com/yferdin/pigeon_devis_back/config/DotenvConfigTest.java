package com.yferdin.pigeon_devis_back.config;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class DotenvConfigTest {

    @BeforeAll
    static void setup() {
        System.setProperty("spring.profiles.active", "test");
    }

    @Value("${spring.datasource.url}")
    private String dbUrl;

    @Value("${spring.datasource.username}")
    private String dbUsername;

    @Value("${app.jwt.secret}")
    private String jwtSecret;

    @Test
    void shouldLoadEnvironmentVariables() {
        // Vérifie que les variables essentielles sont chargées
        assertNotNull(dbUrl, "L'URL de la base de données devrait être définie");
        assertNotNull(dbUsername, "Le nom d'utilisateur de la base de données devrait être défini");
        assertNotNull(jwtSecret, "Le secret JWT devrait être défini");
        
        // Vérifie les valeurs attendues pour la base de données
        assertTrue(dbUrl.contains("jdbc:h2:mem:testdb"), "L'URL devrait pointer vers une base H2");
        assertEquals("sa", dbUsername, "Le nom d'utilisateur devrait être 'sa'");
        
        // Vérifie que le secret JWT a une longueur suffisante pour être sécurisé
        assertTrue(jwtSecret.length() >= 32, "Le secret JWT devrait avoir au moins 32 caractères");
    }
} 