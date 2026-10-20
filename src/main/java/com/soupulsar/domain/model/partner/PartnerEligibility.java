package com.soupulsar.domain.model.partner;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class PartnerEligibility {

    private final UUID id;
    private final UUID partnerDiscountId;
    private final PartnerEligibilityType type;
    private final String value;


    public static PartnerEligibility create(UUID partnerId, PartnerEligibilityType type, String value) {
        return PartnerEligibility.builder()
                .id(UUID.randomUUID())
                .partnerDiscountId(partnerId)
                .type(type)
                .value(value)
                .build();
    }

    public static PartnerEligibility restore(UUID id, UUID partnerId, PartnerEligibilityType type, String value) {
        return PartnerEligibility.builder()
                .id(id)
                .partnerDiscountId(partnerId)
                .type(type)
                .value(value)
                .build();
    }
}