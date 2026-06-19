package com.soupulsar.domain.repository;

import com.soupulsar.domain.model.auth.PasswordResetToken;

import java.util.Optional;
import java.util.UUID;

public interface PasswordResetTokenRepository {

    PasswordResetToken save(PasswordResetToken passwordResetToken);
    Optional<PasswordResetToken> findById(UUID uuid);
    Optional<PasswordResetToken> findByToken(String token);

}
