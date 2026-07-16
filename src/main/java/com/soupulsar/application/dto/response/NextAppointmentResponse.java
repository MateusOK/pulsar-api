package com.soupulsar.application.dto.response;

import lombok.Builder;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder
public record NextAppointmentResponse(

        UUID id,
        LocalDateTime startsAt,
        LocalDateTime endsAt,
        String patientName,
        String whatsappUrl
) {
}