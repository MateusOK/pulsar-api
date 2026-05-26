package com.soupulsar.domain.repository;

import com.soupulsar.domain.model.payment.WebhookEvent;

public interface WebhookEventRepository {

    boolean existsByExternalEventId(String externalEventId);

    WebhookEvent save(WebhookEvent webhookEvent);

}