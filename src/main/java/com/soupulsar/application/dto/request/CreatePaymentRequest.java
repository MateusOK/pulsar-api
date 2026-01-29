package com.soupulsar.application.dto.request;

import com.soupulsar.domain.model.enums.PaymentMethod;
import com.soupulsar.domain.model.vo.Money;

import java.util.UUID;

public record CreatePaymentRequest(
        UUID sessionId,
        UUID specialistId,
        UUID clientId,
        Money price,
        Money discount,
        PaymentMethod paymentMethod
) {
}