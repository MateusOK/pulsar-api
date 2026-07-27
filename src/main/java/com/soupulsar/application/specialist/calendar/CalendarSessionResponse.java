package com.soupulsar.application.specialist.calendar;

import com.soupulsar.domain.model.enums.SessionStatus;

import java.time.LocalDateTime;
import java.util.UUID;

public record CalendarSessionResponse(

        UUID sessionId,
        LocalDateTime startsAt,
        LocalDateTime endsAt,
        SessionStatus status,

        UUID clientId,
        String clientName

) {
}