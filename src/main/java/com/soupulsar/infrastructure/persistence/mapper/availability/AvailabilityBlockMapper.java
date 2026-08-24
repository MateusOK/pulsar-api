package com.soupulsar.infrastructure.persistence.mapper.availability;

import com.soupulsar.domain.model.availability.AvailabilityBlock;
import com.soupulsar.infrastructure.persistence.entity.availability.AvailabilityBlockEntity;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AvailabilityBlockMapper {

    public static AvailabilityBlockEntity toEntity(AvailabilityBlock availabilityBlock) {
        AvailabilityBlockEntity entity = new AvailabilityBlockEntity();
        entity.setId(availabilityBlock.getId());
        entity.setSpecialistId(availabilityBlock.getSpecialistId());
        entity.setStartsAt(availabilityBlock.getStartsAt());
        entity.setEndsAt(availabilityBlock.getEndsAt());
        entity.setReason(availabilityBlock.getReason());
        return entity;
    }

    public static AvailabilityBlock toModel(AvailabilityBlockEntity entity) {
        return AvailabilityBlock.restore(
                entity.getId(),
                entity.getSpecialistId(),
                entity.getStartsAt(),
                entity.getEndsAt(),
                entity.getReason()
        );
    }
}