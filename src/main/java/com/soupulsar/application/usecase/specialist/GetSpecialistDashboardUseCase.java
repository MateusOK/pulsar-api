package com.soupulsar.application.usecase.specialist;

import com.soupulsar.application.dto.response.DashboardResponse;
import com.soupulsar.application.dto.response.NextAppointmentResponse;
import com.soupulsar.application.dto.response.TodaySummaryResponse;
import com.soupulsar.application.dto.response.WeekSummaryResponse;
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
    private final Clock clock;

    private static final String WHATSAPP_BASE_URL = "https://wa.me/";

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
                .whatsappUrl(generateWhatsAppUrl(patient.getTelephone()))
                .build();
    }

    private TodaySummaryResponse getTodaySummary(UUID specialistId, LocalDateTime currentDateTime) {

        LocalDate today = currentDateTime.toLocalDate();

        LocalDateTime startOfDay = today.atStartOfDay();
        LocalDateTime endOfDay = today.atTime(LocalTime.MAX);

        SessionSummary summary = getSessionSummary(specialistId, startOfDay, endOfDay);

        return TodaySummaryResponse.builder()
                .completedAppointments(summary.completed())
                .remainingAppointments(summary.confirmed())
                .totalAppointments(summary.completed() + summary.confirmed())
                .build();
    }

    private WeekSummaryResponse getWeekSummary(UUID specialistId, LocalDateTime currentDateTime) {

        DateRange currentWeek = getCurrentWeekRange(currentDateTime);

        SessionSummary summary = getSessionSummary(specialistId, currentWeek.start(), currentWeek.end());

        return WeekSummaryResponse.builder()
                .completedAppointments(summary.completed())
                .totalAppointments(summary.completed() + summary.confirmed())
                .build();
    }

    private String generateWhatsAppUrl(String phoneNumber) {

        if (phoneNumber == null || phoneNumber.isEmpty()) {
            return null;
        }

        return WHATSAPP_BASE_URL + phoneNumber.replaceAll("[^\\d]", ""); // Remove non-digit characters
    }

    private Long countCompletedSessions(UUID specialistId, LocalDateTime startTime, LocalDateTime endTime) {
        return sessionRepository.countSessionsByStatus(specialistId, List.of(SessionStatus.COMPLETED), startTime, endTime);
    }

    private Long countConfirmedSessions(UUID specialistId, LocalDateTime startTime, LocalDateTime endTime) {
        return sessionRepository.countSessionsByStatus(specialistId, List.of(SessionStatus.CONFIRMED), startTime, endTime);
    }

    private DateRange getCurrentWeekRange(LocalDateTime currentDateTime) {

        LocalDate today = currentDateTime.toLocalDate();

        return new DateRange(
                today.with(DayOfWeek.MONDAY).atStartOfDay(),
                today.with(DayOfWeek.SUNDAY).atTime(LocalTime.MAX)
        );
    }

    private SessionSummary getSessionSummary(UUID specialistId, LocalDateTime startTime, LocalDateTime endTime) {

        long completed = countCompletedSessions(specialistId, startTime, endTime);
        long confirmed = countConfirmedSessions(specialistId, startTime, endTime);

        return new SessionSummary(completed, confirmed);
    }

    private record SessionSummary(
            long completed,
            long confirmed
    ){}

    private record DateRange(
            LocalDateTime start,
            LocalDateTime end
    ){}
}