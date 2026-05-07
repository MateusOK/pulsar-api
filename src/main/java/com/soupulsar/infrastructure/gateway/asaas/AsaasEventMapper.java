package com.soupulsar.infrastructure.gateway.asaas;

import com.soupulsar.domain.model.enums.GatewayPaymentEvent;
import org.springframework.stereotype.Component;

@Component
public class AsaasEventMapper {

    public GatewayPaymentEvent map(String event) {

        return switch (event) {

            case "PAYMENT_CONFIRMED",
                 "PAYMENT_RECEIVED" -> GatewayPaymentEvent.PAID;

            case "PAYMENT_OVERDUE" -> GatewayPaymentEvent.OVERDUE;

            case "PAYMENT_REFUNDED" -> GatewayPaymentEvent.REFUNDED;

            case "PAYMENT_CREDIT_CARD_CAPTURE_REFUSED",
                 "PAYMENT_REPROVED_BY_RISK_ANALYSIS" -> GatewayPaymentEvent.FAILED;

            default -> GatewayPaymentEvent.IGNORE;
        };
    }
}