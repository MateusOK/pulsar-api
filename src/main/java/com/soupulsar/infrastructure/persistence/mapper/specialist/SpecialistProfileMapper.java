package com.soupulsar.infrastructure.persistence.mapper.specialist;

import com.soupulsar.domain.model.specialist.SpecialistProfile;
import com.soupulsar.infrastructure.persistence.entity.specialist.SpecialistProfileEntity;
import com.soupulsar.infrastructure.persistence.mapper.payment.MoneyMapper;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SpecialistProfileMapper {

    public static SpecialistProfileEntity toEntity(SpecialistProfile specialistProfile) {
        if (specialistProfile == null) return null;

        SpecialistProfileEntity entity = new SpecialistProfileEntity();
        entity.setUserId(specialistProfile.getUserId());
        entity.setRegistrationNumber(RegistrationNumberMapper.toEmbeddable(specialistProfile.getRegistrationNumber()));
        entity.setSessionPrice(MoneyMapper.toEmbeddable(specialistProfile.getSessionPrice()));
        entity.setSpecialistType(specialistProfile.getSpecialistType());
        entity.setPresentation(PresentationMapper.toEmbeddable(specialistProfile.getPresentation()));

        return entity;
    }

    public static SpecialistProfile toModel(SpecialistProfileEntity entity) {
        if (entity == null) return null;

        return SpecialistProfile.builder()
                .userId(entity.getUserId())
                .registrationNumber(RegistrationNumberMapper.toValueObject(entity.getRegistrationNumber()))
                .specialistType(entity.getSpecialistType())
                .sessionPrice(MoneyMapper.toValueObject(entity.getSessionPrice()))
                .presentation(PresentationMapper.toValueObject(entity.getPresentation()))
                .build();
    }
}