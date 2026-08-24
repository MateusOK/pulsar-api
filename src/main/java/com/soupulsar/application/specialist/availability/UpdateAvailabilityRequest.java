package com.soupulsar.application.specialist.availability;

import java.time.LocalTime;

public record UpdateAvailabilityRequest (
        LocalTime startTime,
        LocalTime endTime
) {
}