package com.soupulsar.application.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.UUID;

@Schema(description = "Request DTO for creating specialist availability")
public record CreateAvailabilityRequest(

        @Schema(description = "ID of the specialist", example = "660e8400-e29b-41d4-a716-446655440000", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank
        UUID specialistId,

        @Schema(description = "Day of the week for the availability", example = "MONDAY", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank
        DayOfWeek dayOfWeek,

        @Schema(description = "Start time of the availability", example = "2025-10-13T10:00:00", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank
        LocalTime startTime,

        @Schema(description = "End time of the availability", example = "2025-10-13T10:50:00", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank
        LocalTime endTime
) {
}