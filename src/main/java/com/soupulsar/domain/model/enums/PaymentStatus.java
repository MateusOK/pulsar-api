package com.soupulsar.domain.model.enums;

public enum PaymentStatus {

    CREATED,
    PENDING,
    PAID,
    OVERDUE,
    FAILED,
    REFUNDED,
    CANCELLED;

    public static PaymentStatus fromAsaas(String asaasStatus) {
        return switch (asaasStatus){
            case "PENDING", "AWAITING_RISK_ANALYSIS" -> PaymentStatus.PENDING;
            case "RECEIVED", "CONFIRMED", "RECEIVED_IN_CASH" -> PaymentStatus.PAID;
            case "OVERDUE", "DUNNING_REQUEST" -> PaymentStatus.OVERDUE;
            case "REFUNDED", "REFUND_REQUESTED", "REFUND_IN_PROGRESS" -> PaymentStatus.REFUNDED;
            case "CHARGEBACK_REQUESTED", "CHARGEBACK_DISPUTE" -> PaymentStatus.FAILED;
            default -> PaymentStatus.FAILED;
        };
    }
}