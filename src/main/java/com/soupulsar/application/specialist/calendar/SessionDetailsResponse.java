package com.soupulsar.application.specialist.calendar;

import com.soupulsar.domain.model.enums.SessionStatus;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder
public record SessionDetailsResponse(

        UUID sessionId,

        SessionStatus status,

        LocalDateTime startsAt,
        LocalDateTime endsAt,

        UUID clientId,
        String clientName,
        String clientEmail,
        String clientPhone,

        String whatsappUrl
) {
}