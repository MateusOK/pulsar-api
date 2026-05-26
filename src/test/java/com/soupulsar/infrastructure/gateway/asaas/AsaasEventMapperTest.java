package com.soupulsar.infrastructure.gateway.asaas;

import com.soupulsar.domain.model.enums.GatewayPaymentEvent;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AsaasEventMapperTest {

    private final AsaasEventMapper mapper = new AsaasEventMapper();

    @Test
    void map_null_or_blank_returns_ignore() {
        assertEquals(GatewayPaymentEvent.IGNORE, mapper.map(null));
        assertEquals(GatewayPaymentEvent.IGNORE, mapper.map(""));
        assertEquals(GatewayPaymentEvent.IGNORE, mapper.map("   "));
    }

    @Test
    void map_known_events() {
        assertEquals(GatewayPaymentEvent.PAID, mapper.map("PAYMENT_CONFIRMED"));
        assertEquals(GatewayPaymentEvent.PAID, mapper.map("PAYMENT_RECEIVED"));
        assertEquals(GatewayPaymentEvent.OVERDUE, mapper.map("PAYMENT_OVERDUE"));
        assertEquals(GatewayPaymentEvent.REFUNDED, mapper.map("PAYMENT_REFUNDED"));
        assertEquals(GatewayPaymentEvent.FAILED, mapper.map("PAYMENT_CREDIT_CARD_CAPTURE_REFUSED"));
        assertEquals(GatewayPaymentEvent.FAILED, mapper.map("PAYMENT_REPROVED_BY_RISK_ANALYSIS"));
    }

    @Test
    void map_is_case_insensitive() {
        assertEquals(GatewayPaymentEvent.PAID, mapper.map("payment_received"));
        assertEquals(GatewayPaymentEvent.PAID, mapper.map("Payment_Confirmed"));
    }

    @Test
    void map_unknown_event_returns_ignore() {
        assertEquals(GatewayPaymentEvent.IGNORE, mapper.map("SOME_RANDOM_EVENT"));
    }
}