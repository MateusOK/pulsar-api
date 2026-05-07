package com.soupulsar.domain.model.payment;

import com.soupulsar.domain.model.enums.GatewayPaymentEvent;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;


@Getter
@Builder(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class WebhookEvent {

        private UUID id;
        private String externalEventId;
        private String externalPaymentId;
        private GatewayPaymentEvent eventType;
        private Instant receivedAt;

        public static WebhookEvent create (String externalEventId, String externalPaymentId, GatewayPaymentEvent eventType) {
            if (externalEventId == null || externalEventId.isBlank()) throw new IllegalArgumentException("External Event ID cannot be null or blank");
            if (externalPaymentId == null || externalPaymentId.isBlank()) throw new IllegalArgumentException("External Payment ID cannot be null or blank");
            if (eventType == null) throw new IllegalArgumentException("Event type cannot be null");
            return WebhookEvent.builder()
                    .id(UUID.randomUUID())
                    .externalEventId(externalEventId)
                    .externalPaymentId(externalPaymentId)
                    .eventType(eventType)
                    .receivedAt(Instant.now())
                    .build();
        }

        public static WebhookEvent restore(UUID id, String externalEventId, String externalPaymentId, GatewayPaymentEvent eventType, Instant receivedAt) {
            return WebhookEvent.builder()
                    .id(id)
                    .externalEventId(externalEventId)
                    .externalPaymentId(externalPaymentId)
                    .eventType(eventType)
                    .receivedAt(receivedAt)
                    .build();
        }
}