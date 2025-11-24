package com.soupulsar.application.dto.response;

import java.time.LocalDate;
import java.util.List;

public record DailyAvailabilityResponse(
        LocalDate date,
        List<TimeSlot> slots
) {
}