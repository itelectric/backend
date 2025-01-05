package com.itelectric.backend.domain.entity;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.util.UUID;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "t_users")
public class User implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(length = 32)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String nuit;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(nullable = false, name = "fk_contact_id", referencedColumnName = "id")
    private Contact contact;

    @Embedded
    private Address address;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;
}