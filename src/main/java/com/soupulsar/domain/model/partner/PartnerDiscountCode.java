package com.soupulsar.domain.model.partner;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.Instant;
import java.util.UUID;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class PartnerDiscountCode {

    private final UUID id;
    private final UUID partnerDiscountId;
    private final UUID clientId;

    private final String recipientEmail;
    private final String codeHash;

    private PartnerDiscountCodeStatus status;

    private final Instant expiresAt;
    private Instant redeemedAt;

    public static PartnerDiscountCode create(UUID partnerDiscountId, UUID clientId, String recipientEmail, String codeHash, Instant expiresAt) {
        validateCreationParams(partnerDiscountId, clientId, recipientEmail, codeHash, expiresAt, Instant.now());
        return PartnerDiscountCode.builder()
                .id(UUID.randomUUID())
                .partnerDiscountId(partnerDiscountId)
                .clientId(clientId)
                .recipientEmail(recipientEmail)
                .codeHash(codeHash)
                .status(PartnerDiscountCodeStatus.ACTIVE)
                .expiresAt(expiresAt)
                .build();
    }

    public static PartnerDiscountCode restore(UUID id, UUID partnerDiscountId, UUID clientId, String recipientEmail, String codeHash, PartnerDiscountCodeStatus status, Instant expiresAt, Instant redeemedAt) {
        return PartnerDiscountCode.builder()
                .id(id)
                .partnerDiscountId(partnerDiscountId)
                .clientId(clientId)
                .recipientEmail(recipientEmail)
                .codeHash(codeHash)
                .status(status)
                .expiresAt(expiresAt)
                .redeemedAt(redeemedAt)
                .build();
    }


    public boolean isRedeemableAt(Instant now) {
        return status == PartnerDiscountCodeStatus.ACTIVE && now.isBefore(expiresAt);
    }

    public void redeem(Instant now) {
        if (!isRedeemableAt(now)) {
            throw new IllegalStateException("Discount code is not redeemable at this time.");
        }
        this.status = PartnerDiscountCodeStatus.REDEEMED;
        this.redeemedAt = now;
    }

    public void invalidate() {
        this.status = PartnerDiscountCodeStatus.INVALIDATED;
    }

    private static void validateCreationParams(UUID partnerDiscountId, UUID clientId, String recipientEmail, String codeHash, Instant expiresAt, Instant now) {
        if (partnerDiscountId == null || clientId == null) {
            throw new IllegalArgumentException("Partner Discount ID and Client ID cannot be null");
        }
        if (recipientEmail == null || recipientEmail.isEmpty()) {
            throw new IllegalArgumentException("Recipient email cannot be null or empty");
        }
        if (codeHash == null || codeHash.isEmpty()) {
            throw new IllegalArgumentException("Code hash cannot be null or empty");
        }
        if (expiresAt == null || !expiresAt.isAfter(now)) {
            throw new IllegalArgumentException("Expiration time must be in the future");
        }
    }
}