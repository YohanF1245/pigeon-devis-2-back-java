package com.yferdin.pigeon_devis_back.business.model;

import com.yferdin.pigeon_devis_back.user.model.Address;
import com.yferdin.pigeon_devis_back.user.model.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "businesses")
@Getter
@Setter
@NoArgsConstructor
public class Business {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "business_id")
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id", nullable = false)
    private User owner;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true, length = 14)
    private String siret;

    @Column(name = "ape_code", nullable = false, length = 5)
    private String apeCode;

    @Column(name = "tax_code", nullable = false, length = 50)
    private String taxCode;

    @Column(name = "logo_path")
    private String logoPath;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "address_id", nullable = false)
    private Address address;
} 