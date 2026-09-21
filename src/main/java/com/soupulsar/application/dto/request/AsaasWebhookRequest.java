package com.soupulsar.application.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record AsaasWebhookRequest(String id, String event, Payment payment) {

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Payment(String id, List<Refund> refunds) {
    }
    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Refund(String status) {
    }
}