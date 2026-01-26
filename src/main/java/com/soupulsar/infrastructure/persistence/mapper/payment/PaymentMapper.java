package com.soupulsar.infrastructure.persistence.mapper.payment;

import com.soupulsar.domain.model.payment.Payment;
import com.soupulsar.infrastructure.persistence.entity.payment.PaymentEntity;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PaymentMapper {

    public static PaymentEntity toEntity(Payment payment) {
        if (payment == null) return null;
        return PaymentEntity.builder()
                .sessionId(payment.getSessionId())
                .specialistId(payment.getSpecialistId())
                .clientId(payment.getClientId())
                .paymentAmounts(PaymentAmountsMapper.toEmbeddable(payment.getAmounts()))
                .paymentSplit(PaymentSplitMapper.toEmbeddable(payment.getSplit()))
                .paymentMethod(payment.getPaymentMethod())
                .paymentStatus(payment.getPaymentStatus())
                .paidAt(payment.getPaidAt())
                .createdAt(payment.getCreatedAt())
                .refundedAt(payment.getRefundedAt())
                .updatedAt(payment.getUpdatedAt())
                .build();
    }

    public static Payment toModel(PaymentEntity entity) {
        if (entity == null) return null;
        return Payment.create(
                entity.getSessionId(),
                entity.getSpecialistId(),
                entity.getClientId(),
                PaymentAmountsMapper.toValueObject(entity.getPaymentAmounts()),
                PaymentSplitMapper.toValueObject(entity.getPaymentSplit()),
                entity.getPaymentMethod()
        );
    }
}