package com.soupulsar.infrastructure.persistence.mapper.partner;

import com.soupulsar.domain.model.partner.ClientPartnerBenefit;
import com.soupulsar.infrastructure.persistence.entity.partner.ClientPartnerBenefitEntity;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ClientPartnerBenefitMapper {

    public static ClientPartnerBenefitEntity toEntity(ClientPartnerBenefit clientPartnerBenefit) {
        return ClientPartnerBenefitEntity.builder()
                .id(clientPartnerBenefit.getId())
                .clientId(clientPartnerBenefit.getClientId())
                .partnerId(clientPartnerBenefit.getPartnerId())
                .activatedAt(clientPartnerBenefit.getActivatedAt())
                .active(clientPartnerBenefit.isActive())
                .build();
    }

    public static ClientPartnerBenefit toModel(ClientPartnerBenefitEntity entity) {
        return ClientPartnerBenefit.restore(
                entity.getId(),
                entity.getClientId(),
                entity.getPartnerId(),
                entity.getActivatedAt(),
                entity.isActive()
        );
    }
}