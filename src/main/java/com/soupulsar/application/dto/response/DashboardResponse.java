package com.soupulsar.application.dto.response;

import lombok.Builder;

@Builder
public record DashboardResponse(

        NextAppointmentResponse nextAppointment,
        TodaySummaryResponse todaySummary,
        WeekSummaryResponse weekSummary

) {
}