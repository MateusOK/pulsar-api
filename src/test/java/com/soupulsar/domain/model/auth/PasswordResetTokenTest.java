package com.soupulsar.domain.model.auth;

import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PasswordResetTokenTest {

    @Test
    void create_requiresNonNullUserId() {
        assertThrows(IllegalArgumentException.class, () -> PasswordResetToken.create(null, "t", Instant.now().plusSeconds(3600)));
    }

    @Test
    void create_and_usage_flow() {
        UUID userId = UUID.randomUUID();
        String token = "tok";
        Instant expires = Instant.now().plusSeconds(3600);

        PasswordResetToken prt = PasswordResetToken.create(userId, token, expires);

        assertNotNull(prt.getId());
        assertEquals(userId, prt.getUserId());
        assertEquals(token, prt.getToken());
        assertFalse(prt.isExpired());
        assertTrue(prt.isUsable());

        prt.markUsed();
        assertTrue(prt.isUsed());
        assertNotNull(prt.getUsedAt());

        // cannot mark used twice
        assertThrows(IllegalStateException.class, prt::markUsed);
    }

    @Test
    void expired_token_isExpired() {
        PasswordResetToken prt = PasswordResetToken.create(UUID.randomUUID(), "t", Instant.now().minusSeconds(10));
        assertTrue(prt.isExpired());
        assertFalse(prt.isUsable());
    }
}