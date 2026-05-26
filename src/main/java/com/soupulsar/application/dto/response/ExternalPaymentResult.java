package com.soupulsar.application.dto.response;

public record ExternalPaymentResult(
        String externalReference,
        String paymentUrl
) {
}