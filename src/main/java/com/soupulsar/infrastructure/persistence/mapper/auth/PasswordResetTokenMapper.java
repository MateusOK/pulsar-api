package com.soupulsar.infrastructure.persistence.mapper.auth;

import com.soupulsar.domain.model.auth.PasswordResetToken;
import com.soupulsar.infrastructure.persistence.entity.auth.PasswordResetTokenEntity;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PasswordResetTokenMapper {

    public static PasswordResetTokenEntity toEntity(PasswordResetToken passwordResetToken) {
        PasswordResetTokenEntity entity = new PasswordResetTokenEntity();
        entity.setId(passwordResetToken.getId());
        entity.setUserId(passwordResetToken.getUserId());
        entity.setToken(passwordResetToken.getToken());
        entity.setExpiresAt(passwordResetToken.getExpiresAt());
        entity.setUsedAt(passwordResetToken.getUsedAt());
        entity.setUsed(passwordResetToken.isUsed());
        return entity;
    }

    public static PasswordResetToken toModel(PasswordResetTokenEntity entity) {
        return PasswordResetToken.restore(
                entity.getId(),
                entity.getUserId(),
                entity.getToken(),
                entity.getExpiresAt(),
                entity.getUsedAt(),
                entity.isUsed()
        );
    }
}