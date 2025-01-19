package com.yferdin.pigeon_devis_back.security.validation;

import jakarta.validation.ConstraintValidatorContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StrongPasswordValidatorTest {

    @Mock
    private ConstraintValidatorContext context;

    @Mock
    private ConstraintValidatorContext.ConstraintViolationBuilder builder;

    private StrongPasswordValidator validator;

    @BeforeEach
    void setUp() {
        validator = new StrongPasswordValidator();
        when(context.buildConstraintViolationWithTemplate(anyString())).thenReturn(builder);
    }

    @Test
    void shouldValidateValidPassword() {
        assertTrue(validator.isValid("Test@1234", context));
    }

    @ParameterizedTest
    @ValueSource(strings = {
        "short",           // Too short
        "nouppercase1@",   // No uppercase
        "NOLOWERCASE1@",   // No lowercase
        "NoSpecialChar1",  // No special char
        "NoNumber@abc",    // No number
        " SpaceAtStart@1", // Space at start
        "SpaceAtEnd@1 ",   // Space at end
        "aaaaaa@1A"        // Repetitive sequence
    })
    void shouldRejectInvalidPasswords(String password) {
        assertFalse(validator.isValid(password, context));
        verify(context).disableDefaultConstraintViolation();
        verify(context).buildConstraintViolationWithTemplate(anyString());
    }

    @Test
    void shouldRejectNullPassword() {
        assertFalse(validator.isValid(null, context));
    }

    @Test
    void shouldRejectEmptyPassword() {
        assertFalse(validator.isValid("", context));
    }

    @Test
    void shouldRejectBlankPassword() {
        assertFalse(validator.isValid("   ", context));
    }

    @ParameterizedTest
    @ValueSource(strings = {
        "Test@1234",          // Basic valid password
        "Complex@Password123", // Complex valid password
        "MyP@ssw0rd",         // Another valid password
        "Str0ng!Pass",        // Valid with different special char
        "Test@1234Test@1234"  // Long valid password
    })
    void shouldAcceptValidPasswords(String password) {
        assertTrue(validator.isValid(password, context));
    }
} 