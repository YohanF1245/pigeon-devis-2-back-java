package com.yferdin.pigeon_devis_back.security.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;

public class StrongPasswordValidator implements ConstraintValidator<StrongPassword, String> {

    private static final int MIN_LENGTH = 8;
    private static final Pattern UPPERCASE_PATTERN = Pattern.compile(".*[A-Z].*");
    private static final Pattern LOWERCASE_PATTERN = Pattern.compile(".*[a-z].*");
    private static final Pattern DIGIT_PATTERN = Pattern.compile(".*\\d.*");
    private static final Pattern SPECIAL_CHAR_PATTERN = Pattern.compile(".*[@#$%^&+=!].*");
    private static final Pattern SPACE_PATTERN = Pattern.compile("^\\S.*\\S$");
    private static final Pattern SEQUENCE_PATTERN = Pattern.compile(".*(\\w)\\1{5,}.*");

    @Override
    public void initialize(StrongPassword constraintAnnotation) {
    }

    @Override
    public boolean isValid(String password, ConstraintValidatorContext context) {
        if (password == null || password.trim().isEmpty()) {
            return false;
        }

        context.disableDefaultConstraintViolation();
        
        if (password.length() < MIN_LENGTH) {
            addConstraintViolation(context, "Le mot de passe doit contenir au moins 8 caractères");
            return false;
        }

        if (!UPPERCASE_PATTERN.matcher(password).matches()) {
            addConstraintViolation(context, "Le mot de passe doit contenir au moins une lettre majuscule");
            return false;
        }

        if (!LOWERCASE_PATTERN.matcher(password).matches()) {
            addConstraintViolation(context, "Le mot de passe doit contenir au moins une lettre minuscule");
            return false;
        }

        if (!DIGIT_PATTERN.matcher(password).matches()) {
            addConstraintViolation(context, "Le mot de passe doit contenir au moins un chiffre");
            return false;
        }

        if (!SPECIAL_CHAR_PATTERN.matcher(password).matches()) {
            addConstraintViolation(context, "Le mot de passe doit contenir au moins un caractère spécial (@#$%^&+=!)");
            return false;
        }

        if (!SPACE_PATTERN.matcher(password).matches()) {
            addConstraintViolation(context, "Le mot de passe ne doit pas contenir d'espaces au début ou à la fin");
            return false;
        }

        if (SEQUENCE_PATTERN.matcher(password).matches()) {
            addConstraintViolation(context, "Le mot de passe ne doit pas contenir de séquences répétitives");
            return false;
        }

        return true;
    }

    private void addConstraintViolation(ConstraintValidatorContext context, String message) {
        context.buildConstraintViolationWithTemplate(message)
               .addConstraintViolation();
    }
} 