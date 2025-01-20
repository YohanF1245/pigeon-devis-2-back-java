package com.yferdin.pigeon_devis_back.security.service;

import com.yferdin.pigeon_devis_back.security.dto.AuthResponse;
import com.yferdin.pigeon_devis_back.security.dto.LoginRequest;
import com.yferdin.pigeon_devis_back.security.dto.RegisterRequest;
import com.yferdin.pigeon_devis_back.security.exception.EmailAlreadyExistsException;
import com.yferdin.pigeon_devis_back.security.exception.TokenExpiredException;
import com.yferdin.pigeon_devis_back.security.exception.TokenNotFoundException;
import com.yferdin.pigeon_devis_back.security.jwt.JwtTokenProvider;
import com.yferdin.pigeon_devis_back.security.model.VerificationToken;
import com.yferdin.pigeon_devis_back.security.repository.VerificationTokenRepository;
import com.yferdin.pigeon_devis_back.user.model.User;
import com.yferdin.pigeon_devis_back.user.repository.UserRepository;
import com.yferdin.pigeon_devis_back.user.model.Role;
import com.yferdin.pigeon_devis_back.user.repository.RoleRepository;
import com.yferdin.pigeon_devis_back.user.model.RoleType;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider tokenProvider;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    private final VerificationTokenRepository verificationTokenRepository;
    private final EmailService emailService;

    public AuthResponse login(LoginRequest loginRequest) {
        User user = userRepository.findByEmail(loginRequest.getEmail())
            .orElseThrow(() -> new UsernameNotFoundException("Utilisateur non trouvé"));

        if (!user.isVerified()) {
            throw new DisabledException("Veuillez vérifier votre compte via l'email reçu");
        }

        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                loginRequest.getEmail(),
                loginRequest.getPassword()
            )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = tokenProvider.createToken(authentication);

        return new AuthResponse(jwt, user.getEmail(), user.getFirstName(), user.getLastName());
    }

    @Transactional
    public AuthResponse register(RegisterRequest registerRequest) {
        if (userRepository.existsByEmail(registerRequest.getEmail())) {
            throw new EmailAlreadyExistsException("Un compte existe déjà avec cet email");
        }

        User user = new User();
        user.setEmail(registerRequest.getEmail());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        user.setFirstName(registerRequest.getFirstName());
        user.setLastName(registerRequest.getLastName());
        user.setPhone(registerRequest.getPhone());
        user.setVerified(false);

        Role userRole = roleRepository.findByName(RoleType.ROLE_USER)
            .orElseThrow(() -> new RuntimeException("Erreur: Le rôle USER n'existe pas"));
        user.setRole(userRole);

        user = userRepository.save(user);

        VerificationToken verificationToken = new VerificationToken(user);
        verificationTokenRepository.save(verificationToken);

        try {
            emailService.sendVerificationEmail(user.getEmail(), verificationToken.getToken());
        } catch (MessagingException e) {
            throw new RuntimeException("Erreur lors de l'envoi de l'email de vérification", e);
        }

        return new AuthResponse(null, user.getEmail(), user.getFirstName(), user.getLastName());
    }

    @Transactional
    public void verifyAccount(String token) {
        VerificationToken verificationToken = verificationTokenRepository.findByToken(token)
            .orElseThrow(() -> new TokenNotFoundException("Token de vérification invalide"));

        if (verificationToken.isExpired()) {
            throw new TokenExpiredException("Le token de vérification a expiré");
        }

        User user = verificationToken.getUser();
        user.setVerified(true);
        userRepository.save(user);
        verificationTokenRepository.delete(verificationToken);
    }

    public User getUserFromUserDetails(UserDetails userDetails) {
        return userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("Utilisateur non trouvé"));
    }
} 