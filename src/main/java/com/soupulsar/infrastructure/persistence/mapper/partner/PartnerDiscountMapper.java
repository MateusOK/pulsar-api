package com.soupulsar.infrastructure.persistence.mapper.partner;

import com.soupulsar.domain.model.partner.PartnerDiscount;
import com.soupulsar.infrastructure.persistence.entity.partner.PartnerDiscountEntity;
import com.soupulsar.infrastructure.persistence.mapper.payment.PercentageMapper;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PartnerDiscountMapper {

    public static PartnerDiscountEntity toEntity(PartnerDiscount partnerDiscount) {
        if (partnerDiscount == null) return null;
        return PartnerDiscountEntity.builder()
                .id(partnerDiscount.getId())
                .partnerId(partnerDiscount.getPartnerId())
                .percentage(PercentageMapper.toEmbeddable(partnerDiscount.getPercentage()))
                .expiresAt(partnerDiscount.getExpiresAt())
                .active(partnerDiscount.isActive())
                .build();
    }

    public static PartnerDiscount toModel(PartnerDiscountEntity entity) {
        if (entity == null) return null;
        return PartnerDiscount.restore(
                entity.getId(),
                entity.getPartnerId(),
                PercentageMapper.toValueObject(entity.getPercentage()),
                entity.isActive(),
                entity.getExpiresAt()
        );
    }
}