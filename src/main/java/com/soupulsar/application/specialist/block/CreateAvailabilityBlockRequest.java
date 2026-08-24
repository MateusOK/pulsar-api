package com.soupulsar.application.specialist.block;

import java.time.LocalDateTime;
import java.util.UUID;

public record CreateAvailabilityBlockRequest(

        UUID specialistId,
        LocalDateTime startsAt,
        LocalDateTime endsAt,
        String reason

) {
}