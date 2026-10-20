package com.soupulsar.application.partner;

import java.time.Instant;

public record PartnerDiscountCodeIssuedEvent (

        String rawCode,
        String email,
        Instant expiresAt
){
}