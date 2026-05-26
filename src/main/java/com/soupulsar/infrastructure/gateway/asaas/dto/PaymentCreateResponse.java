package com.soupulsar.infrastructure.gateway.asaas.dto;

public record PaymentCreateResponse(
        String id,
        String invoiceUrl
) {
}