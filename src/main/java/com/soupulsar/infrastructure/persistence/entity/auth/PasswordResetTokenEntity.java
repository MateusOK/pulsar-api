package com.soupulsar.infrastructure.persistence.entity.auth;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

@Entity
@Setter
@Getter
@Table(name = "password_reset_tokens")
public class PasswordResetTokenEntity {

    @Id
    private UUID id;

    private UUID userId;

    private String token;

    private Instant expiresAt;

    private Instant usedAt;

    private boolean used;

}