package com.soupulsar.application.dto.request;

import jakarta.validation.constraints.NotBlank;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.UUID;

public record CreateAvailabilityRequest(

        @NotBlank
        UUID specialistId,
        @NotBlank
        DayOfWeek dayOfWeek,
        @NotBlank
        LocalTime startTime,
        @NotBlank
        LocalTime endTime
) {
}