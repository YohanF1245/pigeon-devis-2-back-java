package com.yferdin.pigeon_devis_back.user.repository;

import com.yferdin.pigeon_devis_back.user.model.Role;
import com.yferdin.pigeon_devis_back.user.model.RoleType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(RoleType name);
} 