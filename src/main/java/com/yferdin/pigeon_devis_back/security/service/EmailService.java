package com.yferdin.pigeon_devis_back.security.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    public void sendVerificationEmail(String to, String token) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
        
        helper.setFrom("noreply@pigeon-devis.com");
        helper.setTo(to);
        helper.setSubject("Vérification de votre compte Pigeon Devis");
        
        String content = String.format("""
            <html>
                <body>
                    <h2>Bienvenue sur Pigeon Devis !</h2>
                    <p>Pour activer votre compte, veuillez cliquer sur le lien ci-dessous :</p>
                    <a href="http://localhost:8080/api/auth/verify?token=%s">Vérifier mon compte</a>
                    <p>Ce lien est valable pendant 24 heures.</p>
                    <p>Si vous n'avez pas créé de compte, vous pouvez ignorer cet email.</p>
                </body>
            </html>
            """, token);
        
        helper.setText(content, true);
        mailSender.send(message);
    }
} 