package com.soupulsar.domain.model.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public class PasswordResetToken {

    private UUID id;
    private UUID userId;
    private String token;
    private Instant expiresAt;
    private Instant usedAt;
    private boolean used;

    public boolean isExpired() {
        return expiresAt.isBefore(Instant.now());
    }

    public boolean isUsable() {
        return !used && !isExpired();
    }

    public void markUsed() {
        if (used){
            throw new IllegalStateException("Token already used");
        }
        this.used = true;
        usedAt = Instant.now();
    }

    public static PasswordResetToken create(UUID userId, String token, Instant expiresAt) {
        if (userId == null) throw new IllegalArgumentException("User ID cannot be null");
        return new PasswordResetTokenBuilder()
                .id(UUID.randomUUID())
                .userId(userId)
                .token(token)
                .expiresAt(expiresAt)
                .used(false)
                .usedAt(null)
                .build();
    }

    public static PasswordResetToken restore(UUID id, UUID userId, String token, Instant expiresAt, Instant usedAt, boolean used) {
        return new PasswordResetToken(id, userId, token, expiresAt, usedAt, used);
    }

}