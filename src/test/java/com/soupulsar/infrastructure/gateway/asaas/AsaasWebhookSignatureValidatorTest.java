package com.soupulsar.infrastructure.gateway.asaas;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AsaasWebhookSignatureValidatorTest {

    private AsaasWebhookSignatureValidator validator;

    @BeforeEach
    void setup() {
        validator = new AsaasWebhookSignatureValidator("expected-token");
    }

    @Test
    void isValid_returns_false_for_null_or_empty() {
        assertFalse(validator.isValid(null));
        assertFalse(validator.isValid(""));
    }

    @Test
    void isValid_returns_true_when_tokens_match() {
        assertTrue(validator.isValid("expected-token"));
    }

    @Test
    void isValid_returns_false_when_tokens_do_not_match() {
        assertFalse(validator.isValid("wrong-token"));
    }
}