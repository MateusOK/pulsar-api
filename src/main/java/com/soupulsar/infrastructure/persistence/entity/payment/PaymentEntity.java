package com.soupulsar.infrastructure.persistence.entity.payment;

import com.soupulsar.domain.model.enums.PaymentMethod;
import com.soupulsar.domain.model.enums.PaymentStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jdk.jfr.Timestamp;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "payments")
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class PaymentEntity {

    @Id
    private UUID id;

    @Column(nullable = false)
    private UUID sessionId;

    @Column(nullable = false)
    private UUID specialistId;

    @Column(nullable = false)
    private UUID clientId;

    @Embedded
    private PaymentAmountsEmbeddable paymentAmounts;

    @Embedded
    private PaymentSplitEmbeddable paymentSplit;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentMethod paymentMethod;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentStatus paymentStatus;

    @Timestamp
    private LocalDateTime paidAt;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @Timestamp
    private LocalDateTime refundedAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}