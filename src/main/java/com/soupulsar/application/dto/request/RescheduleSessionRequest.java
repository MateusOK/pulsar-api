package com.soupulsar.application.dto.request;

import java.time.LocalDateTime;
import java.util.UUID;

public record RescheduleSessionRequest(
        UUID sessionId,
        LocalDateTime startTime,
        LocalDateTime endTime
) {
}