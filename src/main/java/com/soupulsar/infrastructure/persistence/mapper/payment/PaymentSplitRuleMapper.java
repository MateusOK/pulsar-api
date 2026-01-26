package com.soupulsar.infrastructure.persistence.mapper.payment;

import com.soupulsar.domain.model.payment.PaymentSplitRule;
import com.soupulsar.infrastructure.persistence.entity.payment.PaymentSplitRuleEntity;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PaymentSplitRuleMapper {

    public static PaymentSplitRuleEntity toEntity(PaymentSplitRule paymentSplitRule) {
        if (paymentSplitRule == null) return null;

        return PaymentSplitRuleEntity.builder()
                .scope(paymentSplitRule.getScope())
                .specialistId(paymentSplitRule.getSpecialistId())
                .specialistType(paymentSplitRule.getSpecialistType())
                .platformPercentage(PercentageMapper.toEmbeddable(paymentSplitRule.getPlatformPercentage()))
                .active(paymentSplitRule.isActive())
                .createdAt(paymentSplitRule.getCreatedAt())
                .build();
    }

    public static PaymentSplitRule toModel(PaymentSplitRuleEntity entity) {
        if (entity == null) return null;

        return PaymentSplitRule.restore(
                entity.getId(),
                entity.getScope(),
                entity.getSpecialistId(),
                entity.getSpecialistType(),
                PercentageMapper.toValueObject(entity.getPlatformPercentage()),
                entity.isActive(),
                entity.getCreatedAt()
        );
    }
}