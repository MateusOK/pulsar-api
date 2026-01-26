package com.soupulsar.domain.model.vo;

import lombok.Value;

@Value
public class PaymentSplit {

    Money platformAmount;
    Money specialistAmount;

    public PaymentSplit(Money platformAmount, Money specialistAmount) {

        if (platformAmount.isNegative() || specialistAmount.isNegative()) {
            throw new IllegalArgumentException("Split amounts cannot be negative");
        }

        this.platformAmount = platformAmount;
        this.specialistAmount = specialistAmount;
    }

    public Money totalAmount() {
        return platformAmount.add(specialistAmount);
    }
}