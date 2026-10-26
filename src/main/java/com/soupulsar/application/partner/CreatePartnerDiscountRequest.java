package com.soupulsar.application.partner;

import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record CreatePartnerDiscountRequest(

    @NotNull
    UUID partnerId,

    @NotNull
    BigDecimal discountPercentage,

    @NotNull
    Instant expiresAt
) {
}