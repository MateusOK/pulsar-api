package com.soupulsar.infrastructure.persistence.repository;

import com.soupulsar.infrastructure.persistence.entity.payment.WebhookEventEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface WebhookEventJpaRepository extends JpaRepository<WebhookEventEntity, UUID> {

    boolean existsByExternalEventId(String externalEventId);

}