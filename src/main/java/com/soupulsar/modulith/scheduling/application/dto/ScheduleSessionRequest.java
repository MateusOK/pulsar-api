package com.soupulsar.modulith.scheduling.application.dto;

import jakarta.validation.constraints.NotBlank;

import java.time.LocalDateTime;
import java.util.UUID;

public record ScheduleSessionRequest(

        @NotBlank
        UUID clientId,
        @NotBlank
        UUID specialistId,
        @NotBlank
        LocalDateTime startTime,
        @NotBlank
        LocalDateTime endTime

) {
}