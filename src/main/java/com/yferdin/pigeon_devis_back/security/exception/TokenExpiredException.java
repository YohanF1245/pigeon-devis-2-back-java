package com.yferdin.pigeon_devis_back.security.exception;

public class TokenExpiredException extends RuntimeException {
    public TokenExpiredException(String message) {
        super(message);
    }
} 