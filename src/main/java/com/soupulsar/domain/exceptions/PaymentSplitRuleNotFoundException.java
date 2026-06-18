package com.soupulsar.domain.exceptions;

public class PaymentSplitRuleNotFoundException extends NotFoundException {

    public PaymentSplitRuleNotFoundException() {
        super("No applicable payment split rule found");
    }
}