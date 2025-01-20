package com.yferdin.pigeon_devis_back.security.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.MailException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    @Value("${app.base-url}")
    private String baseUrl;

    @Value("${spring.mail.username}")
    private String fromEmail;

    public void sendVerificationEmail(String to, String token) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo(to);
            message.setSubject("Vérification de votre compte Pigeon Devis");
            
            String verificationUrl = baseUrl + "/api/auth/verify?token=" + token;
            String content = "Bienvenue sur Pigeon Devis !\n\n" +
                            "Pour activer votre compte, veuillez cliquer sur le lien ci-dessous :\n" +
                            verificationUrl + "\n\n" +
                            "Ce lien est valable pendant 24 heures.\n" +
                            "Si vous n'avez pas créé de compte, vous pouvez ignorer cet email.";
            
            message.setText(content);
            mailSender.send(message);
        } catch (MailException e) {
            throw new RuntimeException("Erreur lors de l'envoi de l'email : " + e.getMessage(), e);
        }
    }
} 