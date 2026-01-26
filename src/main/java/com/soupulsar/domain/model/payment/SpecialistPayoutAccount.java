package com.soupulsar.domain.model.payment;

import com.soupulsar.domain.model.enums.PaymentProvider;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder(access = AccessLevel.PRIVATE)
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class SpecialistPayoutAccount {

    private final UUID id;
    private final UUID specialistId;
    private final PaymentProvider provider;
    private final String externalAccountId;
    private boolean active;
    private LocalDateTime createdAt;

    private SpecialistPayoutAccount(UUID id, UUID specialistId, PaymentProvider provider, String externalAccountId) {

        if (specialistId == null) throw new IllegalArgumentException("specialistId cannot be null");
        if (provider == null) throw new IllegalArgumentException("provider cannot be null");
        if (externalAccountId == null || externalAccountId.isBlank()) {
            throw new IllegalArgumentException("externalAccountId cannot be null or empty");
        }

        this.id = id;
        this.specialistId = specialistId;
        this.provider = provider;
        this.externalAccountId = externalAccountId;
        this.active = true;
        this.createdAt = LocalDateTime.now();
    }

    public static SpecialistPayoutAccount create(UUID specialistId, PaymentProvider provider, String externalAccountId) {
        return new SpecialistPayoutAccount(UUID.randomUUID(), specialistId, provider, externalAccountId);
    }

    public static SpecialistPayoutAccount restore(UUID id, UUID specialistId, PaymentProvider provider,
                                                  String externalAccountId, boolean active, LocalDateTime createdAt) {
        return new SpecialistPayoutAccount(id, specialistId, provider, externalAccountId, active, createdAt);
    }

    public void deactivate() {
        this.active = false;
    }
}