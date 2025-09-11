package com.soupulsar.modulith.scheduling.application.dto;

import com.soupulsar.modulith.scheduling.domain.model.Session;

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