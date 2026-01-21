package com.soupulsar.infrastructure.persistence.mapper.payment;

import com.soupulsar.domain.model.vo.Money;
import com.soupulsar.infrastructure.persistence.entity.payment.MoneyEmbeddable;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MoneyMapper {

    public static MoneyEmbeddable toEmbeddable(Money money) {
        if (money == null) return null;

        return new MoneyEmbeddable(money.value());
    }

    public static Money toValueObject(MoneyEmbeddable moneyEmbeddable) {
        if (moneyEmbeddable == null) return null;

        return new Money(moneyEmbeddable.getValue());
    }
}