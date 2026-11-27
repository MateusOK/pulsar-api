package com.soupulsar.domain.exceptions;

public class ReviewNotAllowedException extends BusinessRuleException {
    public ReviewNotAllowedException(String message) {
        super(message);
    }
}
