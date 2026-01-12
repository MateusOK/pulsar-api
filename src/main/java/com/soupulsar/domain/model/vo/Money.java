package com.soupulsar.domain.model.vo;

import java.math.BigDecimal;
import java.math.RoundingMode;

public record Money(BigDecimal value) implements Comparable<Money> {

    public Money {
        if (value == null)
            throw new IllegalArgumentException("Money value cannot be null");

        value = value.setScale(2, RoundingMode.HALF_UP);
    }

    public static Money zero() {
        return new Money(BigDecimal.ZERO);
    }

    public Money add(Money other) {
        return new Money(this.value.add(other.value));
    }

    public Money subtract(Money other) {
        return new Money(this.value.subtract(other.value));
    }

    public boolean isNegative() {
        return value.compareTo(BigDecimal.ZERO) < 0;
    }

    public boolean isPositive() {
        return value.compareTo(BigDecimal.ZERO) > 0;
    }

    @Override
    public int compareTo(Money other) {
        return this.value.compareTo(other.value);
    }
}