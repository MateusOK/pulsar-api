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
public class ClientPartnerBenefit {

    private final UUID id;
    private final UUID clientId;
    private final UUID partnerId;

    private Instant activatedAt;

    private boolean active;

    public static ClientPartnerBenefit create(UUID clientId, UUID partnerId, Instant activatedAt) {
        validateCreationParams(clientId, partnerId, activatedAt);
        return ClientPartnerBenefit.builder()
                .id(UUID.randomUUID())
                .clientId(clientId)
                .partnerId(partnerId)
                .activatedAt(activatedAt)
                .active(true)
                .build();
    }

    public static ClientPartnerBenefit restore(UUID id, UUID clientId, UUID partnerId, Instant activatedAt, boolean active) {
        return ClientPartnerBenefit.builder()
                .id(id)
                .clientId(clientId)
                .partnerId(partnerId)
                .activatedAt(activatedAt)
                .active(active)
                .build();
    }

    public void deactivate() {
        this.active = false;
    }

    private static void validateCreationParams(UUID clientId, UUID partnerId, Instant activatedAt) {
        if (clientId == null || partnerId == null) {
            throw new IllegalArgumentException("Client and Partner IDs are required");
        }
        if (activatedAt == null) {
            throw new IllegalArgumentException("Benefit expiration must be after activation");
        }
    }

}