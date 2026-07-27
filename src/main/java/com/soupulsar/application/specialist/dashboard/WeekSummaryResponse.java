package com.soupulsar.application.specialist.dashboard;

import lombok.Builder;

@Builder
public record WeekSummaryResponse(
        long totalAppointments,
        long completedAppointments
) {
}