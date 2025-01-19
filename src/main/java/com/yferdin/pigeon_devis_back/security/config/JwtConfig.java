package com.yferdin.pigeon_devis_back.security.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
public class JwtConfig {

    @Value("${app.jwt.secret}")
    private String secret;

    @Value("${app.jwt.expiration}")
    private long expiration;

    @Value("${app.jwt.header:Authorization}")
    private String header;

    @Value("${app.jwt.prefix:Bearer }")
    private String prefix;
} 