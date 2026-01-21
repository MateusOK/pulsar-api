package com.soupulsar.domain.model.vo;

import java.math.BigDecimal;
import java.math.RoundingMode;

public record Percentage(BigDecimal value) {

    public Percentage {
        if (value == null)
            throw new IllegalArgumentException("Percentage cannot be null");

        value = value.setScale(2, RoundingMode.HALF_UP);

        if (value.compareTo(BigDecimal.ZERO) < 0 ||
                value.compareTo(new BigDecimal("100")) > 0) {
            throw new IllegalArgumentException("Percentage must be between 0 and 100");
        }
    }

    public Money applyTo(Money base) {
        BigDecimal factor = value.divide(new BigDecimal("100"), 4, RoundingMode.HALF_UP);
        return new Money(base.value().multiply(factor));
    }
}