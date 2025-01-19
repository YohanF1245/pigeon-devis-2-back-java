package com.yferdin.pigeon_devis_back.user.repository;

import com.yferdin.pigeon_devis_back.user.model.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface AddressRepository extends JpaRepository<Address, UUID> {
} 