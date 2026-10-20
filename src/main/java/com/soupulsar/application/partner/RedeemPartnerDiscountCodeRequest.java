package com.soupulsar.application.partner;

import jakarta.validation.constraints.NotBlank;

public record RedeemPartnerDiscountCodeRequest(

        @NotBlank
        String code
) {
}