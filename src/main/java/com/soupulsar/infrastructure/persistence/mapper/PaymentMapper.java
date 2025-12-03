package com.soupulsar.infrastructure.persistence.mapper;

import com.soupulsar.domain.model.Payment;
import com.soupulsar.infrastructure.persistence.entity.payment.PaymentEntity;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PaymentMapper {

    public static PaymentEntity toEntity(Payment payment) {
        PaymentEntity entity = new PaymentEntity();
        entity.setSessionId(payment.getSessionId());
        entity.setSpecialistId(payment.getSpecialistId());
        entity.setClientId(payment.getClientId());
        entity.setAmount(payment.getAmount());
        entity.setStatus(payment.getPaymentStatus());
        entity.setPaymentMethod(payment.getPaymentMethod());
        entity.setAsaasPaymentId(payment.getAsaasPaymentId());
        entity.setAsaasInvoiceUrl(payment.getAsaasInvoiceUrl());
        entity.setAsaasTransactionId(payment.getAsaasTransactionId());
        entity.setDueDate(payment.getDueDate());
        entity.setPaidAt(payment.getPaidAt());
        return entity;
    }

    public static Payment toModel(PaymentEntity entity) {
        return Payment.builder()
                .id(entity.getId())
                .sessionId(entity.getSessionId())
                .specialistId(entity.getSpecialistId())
                .clientId(entity.getClientId())
                .amount(entity.getAmount())
                .paymentStatus(entity.getStatus())
                .paymentMethod(entity.getPaymentMethod())
                .asaasPaymentId(entity.getAsaasPaymentId())
                .asaasInvoiceUrl(entity.getAsaasInvoiceUrl())
                .asaasTransactionId(entity.getAsaasTransactionId())
                .dueDate(entity.getDueDate())
                .paidAt(entity.getPaidAt())
                .build();
    }
}