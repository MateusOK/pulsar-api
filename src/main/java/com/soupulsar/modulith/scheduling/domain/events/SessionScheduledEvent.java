package com.soupulsar.modulith.scheduling.domain.events;

import com.soupulsar.modulith.scheduling.domain.model.Session;
import com.soupulsar.modulith.scheduling.domain.model.enums.SessionStatus;

import java.time.LocalDateTime;
import java.util.UUID;

public record SessionScheduledEvent(

        UUID sessionId,
        UUID specialistId,
        UUID clientId,
        LocalDateTime startAt,
        LocalDateTime endAt,
        SessionStatus sessionStatus

) {
    public SessionScheduledEvent(Session event) {
        this(
                event.getSessionId(),
                event.getSpecialistId(),
                event.getClientId(),
                event.getStartAt(),
                event.getEndAt(),
                event.getStatus()
        );
    }
}