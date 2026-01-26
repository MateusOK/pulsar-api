package com.soupulsar.infrastructure.persistence.mapper.specialist;

import com.soupulsar.domain.model.payment.SpecialistPayoutAccount;
import com.soupulsar.infrastructure.persistence.entity.specialist.SpecialistPayoutAccountEntity;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public class SpecialistPayoutAccountMapper {

    public static SpecialistPayoutAccountEntity toEntity(SpecialistPayoutAccount specialistPayoutAccount) {
        if (specialistPayoutAccount == null) return null;

        return SpecialistPayoutAccountEntity.builder()
                .specialistId(specialistPayoutAccount.getSpecialistId())
                .provider(specialistPayoutAccount.getProvider())
                .externalAccountId(specialistPayoutAccount.getExternalAccountId())
                .status(specialistPayoutAccount.isActive())
                .createdAt(specialistPayoutAccount.getCreatedAt())
                .build();
    }

    public static SpecialistPayoutAccount toModel(SpecialistPayoutAccountEntity specialistPayoutAccountEntity) {
        if (specialistPayoutAccountEntity == null) return null;

        return SpecialistPayoutAccount.restore(
                specialistPayoutAccountEntity.getId(),
                specialistPayoutAccountEntity.getSpecialistId(),
                specialistPayoutAccountEntity.getProvider(),
                specialistPayoutAccountEntity.getExternalAccountId(),
                specialistPayoutAccountEntity.isStatus(),
                specialistPayoutAccountEntity.getCreatedAt()
        );
    }
}