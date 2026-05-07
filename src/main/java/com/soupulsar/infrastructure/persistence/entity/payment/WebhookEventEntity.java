package com.soupulsar.infrastructure.persistence.entity.payment;

import com.soupulsar.domain.model.enums.GatewayPaymentEvent;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;
import java.util.UUID;

@Entity
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Table(name = "webhook_events",
uniqueConstraints = {@UniqueConstraint(columnNames = "externalEventId")})
public class WebhookEventEntity {

    @Id
    private UUID id;

    @Column(nullable = false, updatable = false)
    private String externalEventId;

    @Column(nullable = false)
    private String externalPaymentId;

    @Enumerated(EnumType.STRING)
    private GatewayPaymentEvent eventType;

    @Column(nullable = false)
    private Instant receivedAt;
}