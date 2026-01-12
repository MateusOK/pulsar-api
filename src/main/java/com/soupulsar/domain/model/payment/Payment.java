package com.soupulsar.domain.model.payment;

import com.soupulsar.domain.model.enums.PaymentMethod;
import com.soupulsar.domain.model.enums.PaymentStatus;
import com.soupulsar.domain.model.vo.PaymentAmounts;
import com.soupulsar.domain.model.vo.PaymentSplit;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Builder(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Payment {

    private UUID id;
    private UUID sessionId;
    private UUID specialistId;
    private UUID clientId;

    private PaymentAmounts amounts;
    private PaymentSplit split;

    private PaymentMethod paymentMethod;
    private PaymentStatus paymentStatus;

    private LocalDateTime paidAt;
    private LocalDateTime createdAt;
    private LocalDateTime refundedAt;
    private LocalDateTime updatedAt;

    public static Payment create(UUID sessionId, UUID specialistId, UUID clientId, PaymentAmounts amounts, PaymentSplit split, PaymentMethod paymentMethod) {

        validateCreationParams(sessionId, specialistId, clientId, paymentMethod);
        return Payment.builder()
                .id(UUID.randomUUID())
                .sessionId(sessionId)
                .specialistId(specialistId)
                .clientId(clientId)
                .paymentMethod(paymentMethod)
                .amounts(amounts)
                .split(split)
                .paymentStatus(PaymentStatus.CREATED)
                .createdAt(LocalDateTime.now())
                .build();
    }

    public void markAsPending(){
        if (paymentStatus != PaymentStatus.CREATED){
            throw new IllegalStateException("Only CREATED payments can be marked as PENDING");
        } else {
            this.paymentStatus = PaymentStatus.PENDING;
            this.updatedAt = LocalDateTime.now();
        }
    }

    public void markAsPaid(){
        if (paymentStatus != PaymentStatus.PENDING){
            throw new IllegalStateException("Only PENDING payments can be marked as PAID");
        } else {
            this.paymentStatus = PaymentStatus.PAID;
            this.paidAt = LocalDateTime.now();
            this.updatedAt = LocalDateTime.now();
        }
    }

    public void markAsFailed(){
        if (paymentStatus != PaymentStatus.PENDING){
            throw new IllegalStateException("Only PENDING payments can be marked as FAILED");
        }
        this.paymentStatus = PaymentStatus.FAILED;
        this.updatedAt = LocalDateTime.now();
    }

    public void changePaymentMethod(PaymentMethod newMethod){
        if (paymentStatus != PaymentStatus.CREATED && paymentStatus != PaymentStatus.PENDING){
            throw new IllegalStateException("Payment method can only be changed before payment is completed");
        }
        if (newMethod == null){
            throw new IllegalArgumentException("New payment method cannot be null");
        }
        this.paymentMethod = newMethod;
        this.updatedAt = LocalDateTime.now();
    }

    public void markAsRefunded(LocalDateTime sessionDateTime){
        if (paymentStatus != PaymentStatus.PAID){
            throw new IllegalStateException("Only PAID payments can be marked as REFUNDED");
        }
        if (!canBeRefunded(sessionDateTime)){
            throw new IllegalStateException("Payment cannot be refunded after the refund window");
        }
        this.paymentStatus = PaymentStatus.REFUNDED;
        this.refundedAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public boolean canBeRefunded(LocalDateTime sessionDateTime) {
        return paymentStatus == PaymentStatus.PAID &&
                paidAt != null &&
                LocalDateTime.now().isBefore(sessionDateTime.minusHours(24));
    }

    private static void validateCreationParams(UUID sessionId, UUID specialistId, UUID clientId, PaymentMethod paymentMethod) {
        if (sessionId == null) {
            throw new IllegalArgumentException("Session ID cannot be null");
        }
        if (specialistId == null) {
            throw new IllegalArgumentException("Specialist ID cannot be null");
        }
        if (clientId == null) {
            throw new IllegalArgumentException("Client ID cannot be null");
        }
        if (paymentMethod == null) {
            throw new IllegalArgumentException("Payment method cannot be null");
        }
    }
}