package com.soupulsar.application.dto.response;

import lombok.Builder;

@Builder
public record WeekSummaryResponse(
        long totalAppointments,
        long completedAppointments
) {
}