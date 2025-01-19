package com.yferdin.pigeon_devis_back.business.exception;

public class SiretAlreadyExistsException extends RuntimeException {
    public SiretAlreadyExistsException(String message) {
        super(message);
    }
} 