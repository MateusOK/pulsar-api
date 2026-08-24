package com.soupulsar.application.specialist.block;

import com.soupulsar.domain.model.availability.AvailabilityBlock;

import java.time.LocalDateTime;
import java.util.UUID;

public record CreateAvailabilityBlockResponse(

        UUID id,
        UUID specialistId,
        LocalDateTime startsAt,
        LocalDateTime endsAt

) {

    public CreateAvailabilityBlockResponse(AvailabilityBlock response){
        this(
                response.getId(),
                response.getSpecialistId(),
                response.getStartsAt(),
                response.getEndsAt()

        );
    }
}