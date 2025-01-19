package com.yferdin.pigeon_devis_back.business.repository;

import com.yferdin.pigeon_devis_back.business.model.Business;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface BusinessRepository extends JpaRepository<Business, UUID> {
    boolean existsBySiret(String siret);
} 