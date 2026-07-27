package com.soupulsar.application.specialist.dashboard;

import lombok.Builder;

@Builder
public record DashboardResponse(

        NextAppointmentResponse nextAppointment,
        TodaySummaryResponse todaySummary,
        WeekSummaryResponse weekSummary

) {
}