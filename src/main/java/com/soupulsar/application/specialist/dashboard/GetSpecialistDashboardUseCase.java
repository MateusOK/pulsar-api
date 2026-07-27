package com.soupulsar.application.specialist.dashboard;

import com.soupulsar.application.specialist.shared.WhatsAppLinkGenerator;
import com.soupulsar.application.specialist.shared.daterange.DateRangeFactory;
import com.soupulsar.application.specialist.shared.summary.SessionStatisticsCalculator;
import com.soupulsar.application.specialist.shared.summary.SessionSummary;
import com.soupulsar.application.utils.SecurityUtils;
import com.soupulsar.domain.exceptions.UserNotFoundException;
import com.soupulsar.domain.model.enums.SessionStatus;
import com.soupulsar.domain.model.session.Session;
import com.soupulsar.domain.model.user.User;
import com.soupulsar.domain.repository.SessionRepository;
import com.soupulsar.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.*;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
public class GetSpecialistDashboardUseCase {

    private final SessionRepository  sessionRepository;
    private final UserRepository userRepository;
    private final SecurityUtils securityUtils;
    private final DateRangeFactory  dateRangeFactory;
    private final SessionStatisticsCalculator sessionStatisticsCalculator;
    private final WhatsAppLinkGenerator whatsAppLinkGenerator;
    private final Clock clock;

    public DashboardResponse execute() {

        UUID specialistId = securityUtils.getCurrentUserId();

        LocalDateTime now = LocalDateTime.now(clock);

        var nextAppointment = getNextAppointment(specialistId, now);
        var todaySummary = getTodaySummary(specialistId, now);
        var weekSummary = getWeekSummary(specialistId, now);

        return DashboardResponse.builder()
                .nextAppointment(nextAppointment)
                .todaySummary(todaySummary)
                .weekSummary(weekSummary)
                .build();
    }

    private NextAppointmentResponse getNextAppointment(UUID specialistId, LocalDateTime currentDateTime) {

        Optional<Session> optionalSession = sessionRepository.findNextSession(specialistId, currentDateTime);

        Session session = optionalSession.orElse(null);

        if(session == null) {
            log.info("No session found for specialist {}", specialistId);
            return null;
        }

        User patient = userRepository.findById(session.getClientId())
                .orElseThrow(() -> new UserNotFoundException("user not found"));

        return NextAppointmentResponse.builder()
                .id(session.getSessionId())
                .startsAt(session.getStartAt())
                .endsAt(session.getEndAt())
                .patientName(patient.getName())
                .whatsappUrl(whatsAppLinkGenerator.generate(patient.getTelephone()))
                .build();
    }

    private TodaySummaryResponse getTodaySummary(UUID specialistId, LocalDateTime currentDateTime) {

        SessionSummary summary = sessionStatisticsCalculator.calculate(specialistId, dateRangeFactory.today(currentDateTime.toLocalDate()));

        return TodaySummaryResponse.builder()
                .completedAppointments(summary.completed())
                .remainingAppointments(summary.confirmed())
                .totalAppointments(summary.total())
                .build();
    }

    private WeekSummaryResponse getWeekSummary(UUID specialistId, LocalDateTime currentDateTime) {

        SessionSummary summary = sessionStatisticsCalculator.calculate(specialistId, dateRangeFactory.week(currentDateTime.toLocalDate()));

        return WeekSummaryResponse.builder()
                .completedAppointments(summary.completed())
                .totalAppointments(summary.total())
                .build();
    }
}