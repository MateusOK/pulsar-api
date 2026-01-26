package com.soupulsar.infrastructure.persistence.mapper.payment;

import com.soupulsar.domain.model.vo.PaymentSplit;
import com.soupulsar.infrastructure.persistence.entity.payment.PaymentSplitEmbeddable;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PaymentSplitMapper {

    public static PaymentSplitEmbeddable toEmbeddable(PaymentSplit paymentSplit) {
        if (paymentSplit == null) return null;

        return PaymentSplitEmbeddable.builder()
                .platformAmount(MoneyMapper.toEmbeddable(paymentSplit.getPlatformAmount()))
                .specialistAmount(MoneyMapper.toEmbeddable(paymentSplit.getSpecialistAmount()))
                .build();
    }

    public static PaymentSplit toValueObject(PaymentSplitEmbeddable embeddable) {
        if (embeddable == null) return null;

        return new PaymentSplit(
                MoneyMapper.toValueObject(embeddable.getPlatformAmount()),
                MoneyMapper.toValueObject(embeddable.getSpecialistAmount())
        );
    }
}