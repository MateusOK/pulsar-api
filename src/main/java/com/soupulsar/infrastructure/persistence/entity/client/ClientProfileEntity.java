package com.soupulsar.infrastructure.persistence.entity.client;

import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "client_profiles")
@Getter
@Setter
public class ClientProfileEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private  UUID profileId;
    private  UUID userId;
    private  Date dateOfBirth;

    @Embedded
    private EmergencyContactEmbeddable emergencyContact;

}