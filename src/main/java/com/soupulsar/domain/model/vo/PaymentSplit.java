package com.soupulsar.domain.model.vo;

import lombok.Value;

@Value
public class PaymentSplit {

    Money platformAmount;
    Money specialistAmount;

    public PaymentSplit(Money platformAmount, Money specialistAmount, Money expectedTotal) {

        if (platformAmount.isNegative() || specialistAmount.isNegative()) {
            throw new IllegalArgumentException("Split amounts cannot be negative");
        }

        Money total = platformAmount.add(specialistAmount);

        if (!total.equals(expectedTotal)) {
            throw new IllegalArgumentException(
                    "Split total must equal payment final amount"
            );
        }

        this.platformAmount = platformAmount;
        this.specialistAmount = specialistAmount;
    }

    public Money totalAmount() {
        return platformAmount.add(specialistAmount);
    }
}