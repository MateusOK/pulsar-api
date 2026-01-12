package com.soupulsar.domain.model.enums;

public enum PaymentSplitScope {
    SPECIALIST(1),
    SPECIALIST_TYPE(2),
    GLOBAL(3);

    private final int priority;

    PaymentSplitScope(int priority) {
        this.priority = priority;
    }

    public int getPriority() {
        return priority;
    }
}