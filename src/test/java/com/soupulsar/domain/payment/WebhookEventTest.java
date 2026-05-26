package com.soupulsar.domain.payment;

import com.soupulsar.domain.model.enums.GatewayPaymentEvent;
import com.soupulsar.domain.model.payment.WebhookEvent;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class WebhookEventTest {

    @Test
    void createSuccessAndRestore() {
        WebhookEvent ev = WebhookEvent.create("evt-1", "pay-1", GatewayPaymentEvent.PAID);
        assertNotNull(ev.getId());
        assertEquals("evt-1", ev.getExternalEventId());
        assertEquals("pay-1", ev.getExternalPaymentId());
        assertEquals(GatewayPaymentEvent.PAID, ev.getEventType());
        assertNotNull(ev.getReceivedAt());

        UUID id = UUID.randomUUID();
        Instant now = Instant.now();
        WebhookEvent restored = WebhookEvent.restore(id, "evt-2", "pay-2", GatewayPaymentEvent.FAILED, now);
        assertEquals(id, restored.getId());
        assertEquals(now, restored.getReceivedAt());
    }

    @Test
    void createInvalidsThrow() {
        assertThrows(IllegalArgumentException.class, () -> WebhookEvent.create(null, "pay", GatewayPaymentEvent.PAID));
        assertThrows(IllegalArgumentException.class, () -> WebhookEvent.create("evt", null, GatewayPaymentEvent.PAID));
        assertThrows(IllegalArgumentException.class, () -> WebhookEvent.create("evt", "pay", null));
    }
}

