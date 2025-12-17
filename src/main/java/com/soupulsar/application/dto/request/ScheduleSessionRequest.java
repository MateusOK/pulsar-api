package com.soupulsar.application.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

import java.time.LocalDateTime;
import java.util.UUID;

@Schema(description = "Request DTO for scheduling a session")
public record ScheduleSessionRequest(

        @Schema(description = "ID of the client scheduling the session", example = "550e8400-e29b-41d4-a716-446655440000", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank
        UUID clientId,

        @Schema(description = "ID of the specialist for the session", example = "660e8400-e29b-41d4-a716-446655440000", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank
        UUID specialistId,

        @Schema(description = "Start time of the session", example = "2024-07-01T10:00:00", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank
        LocalDateTime startTime,

        @Schema(description = "End time of the session", example = "2024-07-01T11:00:00", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank
        LocalDateTime endTime

) {
}