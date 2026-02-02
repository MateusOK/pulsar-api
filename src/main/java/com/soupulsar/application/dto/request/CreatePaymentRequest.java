package com.soupulsar.application.dto.request;

import com.soupulsar.domain.model.enums.PaymentMethod;
import com.soupulsar.domain.model.vo.Money;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record CreatePaymentRequest(

        @NotNull(message = "Session ID cannot be null")
        UUID sessionId,
        @NotNull(message = "Specialist ID cannot be null")
        UUID specialistId,
        @NotNull(message = "Client ID cannot be null")
        UUID clientId,
        @NotNull(message = "Price cannot be null")
        Money price,
        Money discount,
        @NotBlank(message = "Payment method cannot be blank")
        PaymentMethod paymentMethod
) {
}