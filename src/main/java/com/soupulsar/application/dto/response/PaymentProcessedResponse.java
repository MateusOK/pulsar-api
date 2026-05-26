package com.soupulsar.application.dto.response;

import java.util.UUID;

public record PaymentProcessedResponse(
        UUID paymentId,
        String paymentUrl
) {
}