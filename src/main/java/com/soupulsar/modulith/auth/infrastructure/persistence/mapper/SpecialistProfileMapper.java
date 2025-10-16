package com.soupulsar.modulith.auth.infrastructure.persistence.mapper;

import com.soupulsar.modulith.auth.domain.model.SpecialistProfile;
import com.soupulsar.modulith.auth.infrastructure.persistence.entity.SpecialistProfileEntity;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SpecialistProfileMapper {

    public static SpecialistProfileEntity toEntity(SpecialistProfile specialistProfile) {
        if (specialistProfile == null) return null;

        SpecialistProfileEntity entity = new SpecialistProfileEntity();
        entity.setProfileId(specialistProfile.getProfileId());
        entity.setUserId(specialistProfile.getUserId());
        entity.setRegistrationNumber(specialistProfile.getRegistrationNumber());
        entity.setApproaches(specialistProfile.getApproaches());
        entity.setFormations(specialistProfile.getFormations());
        entity.setSpecialties(specialistProfile.getSpecialties());
        entity.setPresentation(PresentationMapper.toEmbeddable(specialistProfile.getPresentation()));

        return entity;
    }


    public static SpecialistProfile toModel(SpecialistProfileEntity entity) {
        if (entity == null) return null;

        return SpecialistProfile.builder()
                .profileId(entity.getProfileId())
                .userId(entity.getUserId())
                .registrationNumber(entity.getRegistrationNumber())
                .formations(entity.getFormations())
                .approaches(entity.getApproaches())
                .specialties(entity.getSpecialties())
                .presentation(PresentationMapper.toValueObject(entity.getPresentation()))
                .build();
    }

}
