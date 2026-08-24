package com.soupulsar.application.specialist.availability;

import java.time.DayOfWeek;

public record UpdateAvailabilityDayRequest(
        DayOfWeek dayOfWeek,
        boolean enabled
) {
}