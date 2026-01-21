package com.soupulsar.infrastructure.persistence.mapper.payment;

import com.soupulsar.domain.model.vo.PaymentAmounts;
import com.soupulsar.infrastructure.persistence.entity.payment.PaymentAmountsEmbeddable;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public class PaymentAmountsMapper {

    public static PaymentAmountsEmbeddable toEmbeddable(PaymentAmounts paymentAmounts) {
        if (paymentAmounts == null) return null;
        return PaymentAmountsEmbeddable.builder()
                .originalAmount(MoneyMapper.toEmbeddable(paymentAmounts.getOriginalAmount()))
                .discountAmount(MoneyMapper.toEmbeddable(paymentAmounts.getDiscountAmount()))
                .finalAmount(MoneyMapper.toEmbeddable(paymentAmounts.getFinalAmount()))
                .build();
    }

    public static PaymentAmounts toValueObject(PaymentAmountsEmbeddable embeddable) {
        if (embeddable == null) return null;
        return new PaymentAmounts(MoneyMapper.toValueObject(embeddable.getOriginalAmount()), MoneyMapper.toValueObject(embeddable.getDiscountAmount()));
    }
}