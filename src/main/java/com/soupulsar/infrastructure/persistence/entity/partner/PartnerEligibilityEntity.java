package com.soupulsar.infrastructure.persistence.entity.partner;

import com.soupulsar.domain.model.partner.PartnerEligibilityType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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

@Getter
@Entity
@Builder
@NoArgsConstructor
@Table(name = "partner_eligibility")
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class PartnerEligibilityEntity {

    @Id
    private UUID id;

    @Column(nullable = false)
    private UUID partnerDiscountId;

    @Enumerated(EnumType.STRING)
    private PartnerEligibilityType type;

    private String value;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    @UpdateTimestamp
    @Column(nullable = false)
    private Instant updatedAt;
}