package com.soupulsar.infrastructure.persistence.entity.specialist;

import com.soupulsar.domain.model.enums.PaymentProvider;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jdk.jfr.BooleanFlag;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "specialist_payout_accounts")
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public class SpecialistPayoutAccountEntity {

    @Id
    private UUID id;

    @Column(nullable = false)
    private UUID specialistId;

    @Enumerated(EnumType.STRING)
    private PaymentProvider provider;

    @Column(nullable = false)
    private String externalAccountId;

    @BooleanFlag
    @Column(nullable = false)
    private boolean status;

    @CreationTimestamp
    private LocalDateTime createdAt;
}