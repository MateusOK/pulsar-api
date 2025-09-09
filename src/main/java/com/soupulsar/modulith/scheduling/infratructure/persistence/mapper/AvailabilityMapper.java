package com.soupulsar.modulith.scheduling.infratructure.persistence.mapper;

import com.soupulsar.modulith.scheduling.domain.model.Availability;
import com.soupulsar.modulith.scheduling.infratructure.persistence.entity.AvailabilityEntity;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AvailabilityMapper {

    public static AvailabilityEntity toEntity(Availability availability) {
        AvailabilityEntity entity = new AvailabilityEntity();
        entity.setUuid(availability.getId());
        entity.setSpecialistId(availability.getSpecialistId());
        entity.setDayOfWeek(availability.getDayOfWeek());
        entity.setStartTime(availability.getStartTime());
        entity.setEndTime(availability.getEndTime());
        return entity;
    }

    public static  Availability toModel(AvailabilityEntity entity) {
        return new Availability(
                entity.getUuid(),
                entity.getSpecialistId(),
                entity.getDayOfWeek(),
                entity.getStartTime(),
                entity.getEndTime()
        );
    }

}