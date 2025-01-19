package com.yferdin.pigeon_devis_back.config;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("dev")
class DotenvConfigTest {

    @Test
    void shouldLoadEnvironmentVariables() {
        // Vérifie que les variables essentielles sont chargées
        assertNotNull(System.getProperty("DB_HOST"), "DB_HOST devrait être défini");
        assertNotNull(System.getProperty("DB_PORT"), "DB_PORT devrait être défini");
        assertNotNull(System.getProperty("DB_NAME"), "DB_NAME devrait être défini");
        assertNotNull(System.getProperty("DB_USER"), "DB_USER devrait être défini");
        assertNotNull(System.getProperty("JWT_SECRET"), "JWT_SECRET devrait être défini");
        
        // Vérifie les valeurs attendues
        assertEquals("localhost", System.getProperty("DB_HOST"));
        assertEquals("5432", System.getProperty("DB_PORT"));
        assertEquals("pigeon-devis", System.getProperty("DB_NAME"));
        assertEquals("pigeon-devis", System.getProperty("DB_USER"));
    }
} 