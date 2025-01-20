package com.yferdin.pigeon_devis_back.user.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import java.util.UUID;

@Entity
@Table(name = "roles")
@Getter
@Setter
@NoArgsConstructor
public class Role {
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(name = "role_id", columnDefinition = "UUID")
    private UUID id;

    @Convert(converter = RoleTypeConverter.class)
    @Column(name = "name", unique = true, nullable = false)
    private RoleType name;

    public Role(RoleType name) {
        this.name = name;
    }
} 