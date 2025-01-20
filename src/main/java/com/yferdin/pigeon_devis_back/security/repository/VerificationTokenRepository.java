package com.yferdin.pigeon_devis_back.security.repository;

import com.yferdin.pigeon_devis_back.security.model.VerificationToken;
import com.yferdin.pigeon_devis_back.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VerificationTokenRepository extends JpaRepository<VerificationToken, Long> {
    Optional<VerificationToken> findByToken(String token);
    Optional<VerificationToken> findByUser(User user);
} 