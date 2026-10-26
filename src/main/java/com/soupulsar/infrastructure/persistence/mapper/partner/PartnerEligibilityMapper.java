package com.soupulsar.infrastructure.persistence.mapper.partner;

import com.soupulsar.domain.model.partner.PartnerEligibility;
import com.soupulsar.infrastructure.persistence.entity.partner.PartnerEligibilityEntity;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public class PartnerEligibilityMapper {

    public static PartnerEligibilityEntity toEntity(PartnerEligibility partnerEligibility) {
        if (partnerEligibility == null) return null;
        return PartnerEligibilityEntity.builder()
                .id(partnerEligibility.getId())
                .partnerDiscountId(partnerEligibility.getPartnerDiscountId())
                .type(partnerEligibility.getType())
                .value(partnerEligibility.getValue())
                .build();
    }

    public  static PartnerEligibility toModel(PartnerEligibilityEntity entity) {
        if (entity == null) return null;
        return PartnerEligibility.restore(
                entity.getId(),
                entity.getPartnerDiscountId(),
                entity.getType(),
                entity.getValue()
        );
    }
}