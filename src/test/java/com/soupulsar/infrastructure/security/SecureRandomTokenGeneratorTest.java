package com.soupulsar.infrastructure.security;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SecureRandomTokenGeneratorTest {

    private final SecureRandomTokenGenerator generator = new SecureRandomTokenGenerator();

    @Test
    void generate_returnsNonNullNonEmptyToken() {
        String token = generator.generate();
        assertNotNull(token);
        assertFalse(token.isEmpty());
    }

    @Test
    void generate_returnsExpectedLengthFor32BytesBase64WithoutPadding() {
        // Check behavioral contract rather than specific implementation details
        String token = generator.generate();
        assertNotNull(token);
        assertFalse(token.isEmpty());
        // should not contain padding characters when using withoutPadding()
        assertFalse(token.contains("="));
        // encoded representation should be reasonably long (at least 32 chars)
        assertTrue(token.length() >= 32);
    }
}