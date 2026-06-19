package com.soupulsar.infrastructure.persistence.mapper.auth;

import com.soupulsar.domain.model.auth.PasswordResetToken;
import com.soupulsar.infrastructure.persistence.entity.auth.PasswordResetTokenEntity;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PasswordResetTokenMapperTest {

    @Test
    void toEntity_and_back_restoreEquivalent() {
        UUID id = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        String token = "abc";
        Instant expires = Instant.now().plusSeconds(3600);
        Instant usedAt = null;

        PasswordResetToken model = PasswordResetToken.restore(id, userId, token, expires, usedAt, false);

        PasswordResetTokenEntity entity = PasswordResetTokenMapper.toEntity(model);
        assertEquals(id, entity.getId());
        assertEquals(userId, entity.getUserId());
        assertEquals(token, entity.getToken());

        PasswordResetToken restored = PasswordResetTokenMapper.toModel(entity);
        assertEquals(model.getId(), restored.getId());
        assertEquals(model.getUserId(), restored.getUserId());
        assertEquals(model.getToken(), restored.getToken());
        assertEquals(model.isUsed(), restored.isUsed());
    }

    @Test
    void toEntity_and_back_withUsedAndUsedAt() {
        UUID id = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        String token = "used-token";
        Instant expires = Instant.now().plusSeconds(3600);
        Instant usedAt = Instant.now();

        PasswordResetToken model = PasswordResetToken.restore(id, userId, token, expires, usedAt, true);

        PasswordResetTokenEntity entity = PasswordResetTokenMapper.toEntity(model);
        assertTrue(entity.isUsed());
        assertNotNull(entity.getUsedAt());

        PasswordResetToken restored = PasswordResetTokenMapper.toModel(entity);
        assertTrue(restored.isUsed());
        assertNotNull(restored.getUsedAt());
    }
}