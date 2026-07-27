package com.soupulsar.application.specialist.dashboard;

import lombok.Builder;

@Builder
public record TodaySummaryResponse(

        long totalAppointments,
        long completedAppointments,
        long remainingAppointments
) {
}