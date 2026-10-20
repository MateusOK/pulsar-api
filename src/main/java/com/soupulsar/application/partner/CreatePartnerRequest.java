package com.soupulsar.application.partner;

import jakarta.validation.constraints.NotBlank;

public record CreatePartnerRequest(

    @NotBlank
    String name
) {
}