package com.soupulsar.application.partner;

import com.soupulsar.domain.model.partner.PartnerEligibilityType;
import jakarta.validation.constraints.NotBlank;

import java.util.UUID;

public record CreatePartnerEligibilityRequest(

    @NotBlank
    UUID partnerId,
    @NotBlank
    PartnerEligibilityType type,
    @NotBlank
    String value
) {
}