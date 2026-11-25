package com.soupulsar.application.client;

import com.soupulsar.domain.model.enums.SessionStatus;
import com.soupulsar.domain.model.enums.SpecialistType;
import com.soupulsar.domain.model.vo.RegistrationNumber;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder
public record ClientSessionResponse(
        UUID sessionId,
        LocalDateTime startsAt,
        LocalDateTime endsAt,
        SessionStatus status,
        UUID specialistId,
        String specialistName,
        RegistrationNumber registrationNumber,
        SpecialistType specialistType
) {
}
