package com.soupulsar.infrastructure.persistence.entity.partner;

import com.soupulsar.infrastructure.persistence.entity.payment.PercentageEmbeddable;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
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
@Table(name = "partner_discounts")
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class PartnerDiscountEntity {

    @Id
    private UUID id;

    @Column(nullable = false)
    private UUID partnerId;

    @Embedded
    private PercentageEmbeddable percentage;

    @Column(nullable = false)
    private boolean active;

    @Column(nullable = false)
    private Instant expiresAt;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    @UpdateTimestamp
    @Column(nullable = false)
    private Instant updatedAt;
}