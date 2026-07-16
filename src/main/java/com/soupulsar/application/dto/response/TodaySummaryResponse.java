package com.soupulsar.application.dto.response;

import lombok.Builder;

@Builder
public record TodaySummaryResponse(

        long totalAppointments,
        long completedAppointments,
        long remainingAppointments
) {
}