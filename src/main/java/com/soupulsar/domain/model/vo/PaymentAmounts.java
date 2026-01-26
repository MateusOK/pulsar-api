package com.soupulsar.domain.model.vo;

import lombok.Value;

@Value
public class PaymentAmounts {

    Money originalAmount;
    Money discountAmount;
    Money finalAmount;

    public PaymentAmounts(Money originalAmount, Money discountAmount) {

        if (discountAmount.isNegative()) {
            throw new IllegalArgumentException("Discount cannot be negative");
        }

        Money calculatedFinal = originalAmount.subtract(discountAmount);

        if (!calculatedFinal.isPositive()) {
            throw new IllegalArgumentException("Final amount must be greater than zero");
        }

        this.originalAmount = originalAmount;
        this.discountAmount = discountAmount;
        this.finalAmount = calculatedFinal;
    }
}