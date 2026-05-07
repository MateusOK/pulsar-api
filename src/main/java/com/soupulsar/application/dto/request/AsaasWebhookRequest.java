package com.soupulsar.application.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record AsaasWebhookRequest(String id, String event, Payment payment) {

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Payment(String id) {
    }
}