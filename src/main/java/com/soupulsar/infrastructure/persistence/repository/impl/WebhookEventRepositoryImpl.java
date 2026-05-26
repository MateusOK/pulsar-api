package com.soupulsar.infrastructure.persistence.repository.impl;

import com.soupulsar.domain.model.payment.WebhookEvent;
import com.soupulsar.domain.repository.WebhookEventRepository;
import com.soupulsar.infrastructure.persistence.entity.payment.WebhookEventEntity;
import com.soupulsar.infrastructure.persistence.mapper.payment.WebhookEventMapper;
import com.soupulsar.infrastructure.persistence.repository.WebhookEventJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class WebhookEventRepositoryImpl implements WebhookEventRepository {

    private final WebhookEventJpaRepository jpaRepository;

    @Override
    public boolean existsByExternalEventId(String externalEventId) {
        return jpaRepository.existsByExternalEventId(externalEventId);
    }

    @Override
    public WebhookEvent save(WebhookEvent webhookEvent) {
        WebhookEventEntity webhookEventEntity = WebhookEventMapper.toEntity(webhookEvent);
        WebhookEventEntity savedWebhookEventEntity = jpaRepository.save(webhookEventEntity);
        return WebhookEventMapper.toModel(savedWebhookEventEntity);
    }
}