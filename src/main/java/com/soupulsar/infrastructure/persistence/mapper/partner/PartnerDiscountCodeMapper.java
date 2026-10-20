package com.soupulsar.infrastructure.persistence.mapper.partner;

import com.soupulsar.domain.model.partner.PartnerDiscountCode;
import com.soupulsar.infrastructure.persistence.entity.partner.PartnerDiscountCodeEntity;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PartnerDiscountCodeMapper {

    public static PartnerDiscountCodeEntity toEntity(PartnerDiscountCode partnerDiscountCode) {
        return PartnerDiscountCodeEntity.builder()
                .id(partnerDiscountCode.getId())
                .clientId(partnerDiscountCode.getClientId())
                .partnerDiscountId(partnerDiscountCode.getPartnerDiscountId())
                .codeHash(partnerDiscountCode.getCodeHash())
                .recipientEmail(partnerDiscountCode.getRecipientEmail())
                .status(partnerDiscountCode.getStatus())
                .expiresAt(partnerDiscountCode.getExpiresAt())
                .redeemedAt(partnerDiscountCode.getRedeemedAt())
                .build();
    }

    public static PartnerDiscountCode toModel(PartnerDiscountCodeEntity entity) {
        return PartnerDiscountCode.restore(
                entity.getId(),
                entity.getPartnerDiscountId(),
                entity.getClientId(),
                entity.getRecipientEmail(),
                entity.getCodeHash(),
                entity.getStatus(),
                entity.getExpiresAt(),
                entity.getRedeemedAt()
        );
    }
}