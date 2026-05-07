package com.soupulsar.infrastructure.persistence.mapper.payment;

import com.soupulsar.domain.model.payment.WebhookEvent;
import com.soupulsar.infrastructure.persistence.entity.payment.WebhookEventEntity;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class WebhookEventMapper {

    public static WebhookEventEntity toEntity(WebhookEvent webhookEvent) {
        if (webhookEvent == null) return null;
        return WebhookEventEntity.builder()
                .id(webhookEvent.getId())
                .externalPaymentId(webhookEvent.getExternalPaymentId())
                .externalEventId(webhookEvent.getExternalEventId())
                .eventType(webhookEvent.getEventType())
                .receivedAt(webhookEvent.getReceivedAt())
                .build();
    }

    public static WebhookEvent toModel(WebhookEventEntity entity) {
        if (entity == null) return null;
        return WebhookEvent.restore(
                entity.getId(),
                entity.getExternalEventId(),
                entity.getExternalPaymentId(),
                entity.getEventType(),
                entity.getReceivedAt()
        );
    }
}