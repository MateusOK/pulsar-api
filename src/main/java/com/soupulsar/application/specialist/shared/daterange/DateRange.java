package com.soupulsar.application.specialist.shared.daterange;

import java.time.LocalDateTime;

public record DateRange(
        LocalDateTime start,
        LocalDateTime end
) {
}