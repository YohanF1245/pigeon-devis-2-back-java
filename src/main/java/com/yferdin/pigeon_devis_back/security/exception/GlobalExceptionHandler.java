package com.yferdin.pigeon_devis_back.security.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

import com.yferdin.pigeon_devis_back.business.exception.SiretAlreadyExistsException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error -> 
            errors.put(error.getField(), error.getDefaultMessage())
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }

    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ResponseEntity<Map<String, String>> handleEmailAlreadyExists(EmailAlreadyExistsException ex) {
        Map<String, String> error = new HashMap<>();
        error.put("email", ex.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
    }

    @ExceptionHandler({BadCredentialsException.class, UsernameNotFoundException.class})
    public ResponseEntity<Map<String, String>> handleAuthenticationException(Exception ex) {
        Map<String, String> error = new HashMap<>();
        error.put("error", "Email ou mot de passe incorrect");
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
    }

    @ExceptionHandler(SiretAlreadyExistsException.class)
    public ResponseEntity<Map<String, String>> handleSiretAlreadyExists(SiretAlreadyExistsException ex) {
        Map<String, String> error = new HashMap<>();
        error.put("siret", ex.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
    }
} 