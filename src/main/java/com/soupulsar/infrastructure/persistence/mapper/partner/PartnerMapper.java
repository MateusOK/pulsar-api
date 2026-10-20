package com.soupulsar.infrastructure.persistence.mapper.partner;

import com.soupulsar.domain.model.partner.Partner;
import com.soupulsar.infrastructure.persistence.entity.partner.PartnerEntity;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PartnerMapper {

    public static PartnerEntity toEntity(Partner partner) {
        if (partner == null) return null;
        return PartnerEntity.builder()
                .id(partner.getId())
                .name(partner.getName())
                .active(partner.isActive())
                .build();
    }

    public static Partner toModel(PartnerEntity entity) {
        if (entity == null) return null;
        return Partner.restore(
                entity.getId(),
                entity.getName(),
                entity.isActive()
        );
    }
}