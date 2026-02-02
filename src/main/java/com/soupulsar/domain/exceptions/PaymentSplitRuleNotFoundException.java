package com.soupulsar.domain.exceptions;

public class PaymentSplitRuleNotFoundException extends  RuntimeException {

    public PaymentSplitRuleNotFoundException() {
        super("No applicable payment split rule found");
    }
}