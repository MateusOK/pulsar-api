package com.soupulsar.infrastructure.persistence.entity.payment;

import com.soupulsar.domain.model.enums.PaymentSplitScope;
import com.soupulsar.domain.model.enums.SpecialistType;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jdk.jfr.BooleanFlag;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "payment_split_rules")
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor(access =  AccessLevel.PRIVATE)
public class PaymentSplitRuleEntity {

    @Id
    private UUID id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentSplitScope scope;

    @Column(nullable = false)
    private UUID specialistId;

    @Enumerated(EnumType.STRING)
    private SpecialistType specialistType;

    @Embedded
    private PercentageEmbeddable platformPercentage;

    @BooleanFlag
    private boolean active;

    @CreationTimestamp
    private LocalDateTime createdAt;
}