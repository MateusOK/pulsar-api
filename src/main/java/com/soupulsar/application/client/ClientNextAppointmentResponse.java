package com.soupulsar.application.client;

import lombok.Builder;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder
public record ClientNextAppointmentResponse(
        UUID id,
        LocalDateTime startsAt,
        LocalDateTime endsAt,
        String specialistName
) {
}
