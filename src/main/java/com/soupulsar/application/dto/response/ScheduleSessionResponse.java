package com.soupulsar.application.dto.response;

import com.soupulsar.domain.model.session.Session;

import java.time.LocalDateTime;
import java.util.UUID;

public record ScheduleSessionResponse(

        UUID sessionId,
        UUID specialistId,
        UUID clientId,
        LocalDateTime startTime,
        LocalDateTime endTime

) {

    public ScheduleSessionResponse(Session response){
        this(
                response.getSessionId(),
                response.getSpecialistId(),
                response.getClientId(),
                response.getStartAt(),
                response.getEndAt()
        );
    }

}