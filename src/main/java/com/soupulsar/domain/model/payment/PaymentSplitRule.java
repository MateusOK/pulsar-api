package com.soupulsar.domain.model.payment;

import com.soupulsar.domain.model.enums.PaymentSplitScope;
import com.soupulsar.domain.model.enums.SpecialistType;
import com.soupulsar.domain.model.vo.Percentage;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Builder(access = lombok.AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PaymentSplitRule {

    private UUID id;
    private PaymentSplitScope scope;
    private UUID specialistId;
    private SpecialistType specialistType;
    private Percentage platformPercentage;
    private boolean active;
    private LocalDateTime createdAt;

    public static PaymentSplitRule createSpecialistRule(UUID specialistId, Percentage platformPercentage) {
        validateScope(PaymentSplitScope.SPECIALIST, specialistId, null);
        return PaymentSplitRule.builder()
                .id(UUID.randomUUID())
                .scope(PaymentSplitScope.SPECIALIST)
                .specialistId(specialistId)
                .platformPercentage(platformPercentage)
                .active(true)
                .createdAt(LocalDateTime.now())
                .build();
    }

    public static PaymentSplitRule createSpecialistTypeRule(SpecialistType specialistType, Percentage platformPercentage) {
        validateScope(PaymentSplitScope.SPECIALIST_TYPE, null, specialistType);
        return PaymentSplitRule.builder()
                .id(UUID.randomUUID())
                .scope(PaymentSplitScope.SPECIALIST_TYPE)
                .specialistType(specialistType)
                .platformPercentage(platformPercentage)
                .active(true)
                .createdAt(LocalDateTime.now())
                .build();
    }

    public static PaymentSplitRule createGlobalRule(Percentage platformPercentage) {
        validateScope(PaymentSplitScope.GLOBAL, null, null);
        return PaymentSplitRule.builder()
                .id(UUID.randomUUID())
                .scope(PaymentSplitScope.GLOBAL)
                .platformPercentage(platformPercentage)
                .active(true)
                .createdAt(LocalDateTime.now())
                .build();
    }

    public static PaymentSplitRule restore(UUID id, PaymentSplitScope scope, UUID specialistId, SpecialistType specialistType,
                                           Percentage platformPercentage, boolean active, LocalDateTime createdAt) {
        return PaymentSplitRule.builder()
                .id(id)
                .scope(scope)
                .specialistId(specialistId)
                .specialistType(specialistType)
                .platformPercentage(platformPercentage)
                .active(active)
                .createdAt(createdAt)
                .build();
    }

    private static void validateScope(PaymentSplitScope scope, UUID specialistId, SpecialistType specialistType) {
        if (scope == null) {
            throw new IllegalArgumentException("Scope cannot be null");
        }
        switch (scope) {
            case SPECIALIST -> {
                if (specialistId == null)
                    throw new IllegalArgumentException("Specialist rule requires a valid Specialist ID");
                }
            case SPECIALIST_TYPE -> {
                if (specialistType == null)
                    throw new IllegalArgumentException("Specialist Type rule requires a valid Specialist Type");
                }
            case GLOBAL -> {
                if (specialistId != null || specialistType != null)
                    throw new IllegalArgumentException("Global rule should not have Specialist ID or Specialist Type");
            }
        }
    }

    public void deactivate() {
        this.active = false;
    }

}