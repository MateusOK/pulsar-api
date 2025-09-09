package com.soupulsar.modulith.scheduling.application.dto;

import com.soupulsar.modulith.scheduling.domain.model.Availability;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.UUID;

public record CreateAvailabilityResponse(
        UUID id,
        UUID specialistId,
        DayOfWeek dayOfWeek,
        LocalTime startTime,
        LocalTime endTime
) {

    public CreateAvailabilityResponse(Availability response){
        this(
                response.getId(),
                response.getSpecialistId(),
                response.getDayOfWeek(),
                response.getStartTime(),
                response.getEndTime()
        );
    }
}