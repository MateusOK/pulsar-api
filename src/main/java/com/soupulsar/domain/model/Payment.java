package com.soupulsar.domain.model;

import com.soupulsar.domain.model.enums.PaymentMethod;
import com.soupulsar.domain.model.enums.PaymentStatus;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Payment {

    private UUID id;
    private UUID sessionId;
    private UUID specialistId;
    private UUID clientId;
    private BigDecimal amount;
    private PaymentStatus paymentStatus;
    private PaymentMethod paymentMethod;
    private String asaasPaymentId;
    private String asaasInvoiceUrl;
    private String asaasTransactionId;
    private LocalDateTime dueDate;
    private LocalDateTime paidAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public void markAsPaid(){
        this.paymentStatus = PaymentStatus.PAID;
        this.paidAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public void markAsFailed(){
        this.paymentStatus = PaymentStatus.FAILED;
        this.updatedAt = LocalDateTime.now();
    }

    public boolean canBeRefunded(LocalDateTime sessionDateTime){
        return paymentStatus == PaymentStatus.PAID &&
                paidAt != null &&
                sessionDateTime.isBefore(LocalDateTime.now().minusHours(24));
    }
}