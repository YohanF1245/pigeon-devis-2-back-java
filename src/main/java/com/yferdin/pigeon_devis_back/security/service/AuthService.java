package com.yferdin.pigeon_devis_back.security.service;

import com.yferdin.pigeon_devis_back.security.dto.AuthResponse;
import com.yferdin.pigeon_devis_back.security.dto.LoginRequest;
import com.yferdin.pigeon_devis_back.security.dto.RegisterRequest;
import com.yferdin.pigeon_devis_back.security.exception.EmailAlreadyExistsException;
import com.yferdin.pigeon_devis_back.security.jwt.JwtTokenProvider;
import com.yferdin.pigeon_devis_back.user.model.User;
import com.yferdin.pigeon_devis_back.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider tokenProvider;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthResponse login(LoginRequest loginRequest) {
        // Authentifie l'utilisateur avec Spring Security
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                loginRequest.getEmail(),
                loginRequest.getPassword()
            )
        );

        // Stocke l'authentification dans le contexte de sécurité
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Récupère l'utilisateur depuis la base de données
        User user = userRepository.findByEmail(loginRequest.getEmail())
            .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

        // Génère le token JWT
        String jwt = tokenProvider.createToken(authentication);

        // Retourne la réponse avec le token et les informations de l'utilisateur
        return new AuthResponse(jwt, user.getEmail(), user.getFirstName(), user.getLastName());
    }

    @Transactional
    public AuthResponse register(RegisterRequest registerRequest) {
        // Vérifie si l'email existe déjà
        if (userRepository.existsByEmail(registerRequest.getEmail())) {
            throw new EmailAlreadyExistsException("Un compte existe déjà avec cet email");
        }

        // Crée un nouvel utilisateur
        User user = new User();
        user.setEmail(registerRequest.getEmail());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        user.setFirstName(registerRequest.getFirstName());
        user.setLastName(registerRequest.getLastName());
        user.setPhone(registerRequest.getPhone());
        user.setVerified(true); // Pour le développement, on met directement à true

        // Sauvegarde l'utilisateur
        userRepository.save(user);

        // Authentifie le nouvel utilisateur
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                registerRequest.getEmail(),
                registerRequest.getPassword()
            )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Génère le token JWT
        String jwt = tokenProvider.createToken(authentication);

        // Retourne la réponse
        return new AuthResponse(jwt, user.getEmail(), user.getFirstName(), user.getLastName());
    }
} 