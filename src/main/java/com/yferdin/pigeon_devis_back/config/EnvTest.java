package com.yferdin.pigeon_devis_back.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class EnvTest implements CommandLineRunner {

    @Override
    public void run(String... args) {
        System.out.println("\n=== Test des variables d'environnement ===");
        System.out.println("DB_HOST: " + System.getProperty("DB_HOST"));
        System.out.println("DB_PORT: " + System.getProperty("DB_PORT"));
        System.out.println("DB_NAME: " + System.getProperty("DB_NAME"));
        System.out.println("DB_USER: " + System.getProperty("DB_USER"));
        System.out.println("JWT_SECRET: " + System.getProperty("JWT_SECRET"));
        System.out.println("======================================\n");
    }
} 