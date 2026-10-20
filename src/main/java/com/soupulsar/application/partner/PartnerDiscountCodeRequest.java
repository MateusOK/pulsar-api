package com.soupulsar.application.partner;

import jakarta.validation.constraints.NotBlank;

public record PartnerDiscountCodeRequest(

        @NotBlank
        String email
) {
}