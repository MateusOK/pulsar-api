package com.soupulsar.infrastructure.persistence.mapper.availability;

import com.soupulsar.domain.model.availability.Availability;
import com.soupulsar.infrastructure.persistence.entity.availability.AvailabilityEntity;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AvailabilityMapper {

    public static AvailabilityEntity toEntity(Availability availability) {
        AvailabilityEntity entity = new AvailabilityEntity();
        entity.setId(availability.getId());
        entity.setSpecialistId(availability.getSpecialistId());
        entity.setDayOfWeek(availability.getDayOfWeek());
        entity.setStartTime(availability.getStartTime());
        entity.setEndTime(availability.getEndTime());
        return entity;
    }

    public static Availability toModel(AvailabilityEntity entity) {
        return Availability.restore(
                entity.getId(),
                entity.getSpecialistId(),
                entity.getDayOfWeek(),
                entity.isEnabled(),
                entity.getStartTime(),
                entity.getEndTime()
        );
    }
}