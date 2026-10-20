package com.soupulsar.infrastructure.persistence.entity.partner;

import com.soupulsar.domain.model.partner.PartnerDiscountCodeStatus;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.util.UUID;

@Entity
@Getter
@Builder
@NoArgsConstructor
@Table(name = "partner_discount_codes")
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class PartnerDiscountCodeEntity {

    @Id
    private UUID id;

    @Column(nullable = false)
    private UUID partnerDiscountId;

    @Column(nullable = false)
    private UUID clientId;

    @Column(nullable = false)
    private String recipientEmail;

    @Column(nullable = false, unique = true)
    private String codeHash;

    @Enumerated(EnumType.STRING)
    private PartnerDiscountCodeStatus status;

    private Instant expiresAt;
    private Instant redeemedAt;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    @UpdateTimestamp
    private Instant updatedAt;
}