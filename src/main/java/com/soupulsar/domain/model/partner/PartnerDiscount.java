package com.soupulsar.domain.model.partner;

import com.soupulsar.domain.model.vo.Money;
import com.soupulsar.domain.model.vo.Percentage;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class PartnerDiscount {

    private final UUID id;
    private final UUID partnerId;
    private final Percentage percentage;
    private boolean active;
    private final Instant expiresAt;


    public static PartnerDiscount create(UUID partnerId, Percentage percentage, Instant expiresAt, Instant now) {
        validateCreationParams(partnerId, percentage, expiresAt, now);
        return PartnerDiscount.builder()
                .id(UUID.randomUUID())
                .partnerId(partnerId)
                .percentage(percentage)
                .active(true)
                .expiresAt(expiresAt)
                .build();
    }

    public static PartnerDiscount restore(UUID id, UUID partnerId, Percentage percentage, boolean active, Instant expiresAt) {
        return PartnerDiscount.builder()
                .id(id)
                .partnerId(partnerId)
                .percentage(percentage)
                .active(active)
                .expiresAt(expiresAt)
                .build();
    }

    public Money calculateDiscount(Money originalAmount, Instant now) {
        if (!active || !now.isBefore(expiresAt)) {
            return Money.zero();
        }
        return percentage.applyTo(originalAmount);
    }

    public boolean isApplicableAt(Instant now) {
        return active && now.isBefore(expiresAt);
    }

    private static void validateCreationParams(UUID partnerId, Percentage percentage, Instant expiresAt, Instant now) {
        if (partnerId == null) {
            throw new IllegalArgumentException("Partner ID cannot be null");
        }
        if (percentage == null) {
            throw new IllegalArgumentException("Percentage cannot be null");
        }
        if (expiresAt == null || !expiresAt.isAfter(now)) {
            throw new IllegalArgumentException("Expiration date must be in the future");
        }
    }
}