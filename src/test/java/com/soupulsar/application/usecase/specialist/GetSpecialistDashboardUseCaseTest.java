package com.soupulsar.application.usecase.specialist;

import com.soupulsar.application.dto.response.DashboardResponse;
import com.soupulsar.domain.exceptions.UserNotFoundException;
import com.soupulsar.domain.model.enums.SessionStatus;
import com.soupulsar.domain.model.enums.UserRole;
import com.soupulsar.domain.model.enums.UserStatus;
import com.soupulsar.domain.model.session.Session;
import com.soupulsar.domain.model.user.User;
import com.soupulsar.domain.model.vo.Address;
import com.soupulsar.domain.repository.SessionRepository;
import com.soupulsar.domain.repository.UserRepository;
import com.soupulsar.application.utils.SecurityUtils;
import org.junit.jupiter.api.Test;

import java.time.Clock;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.ZoneId;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class GetSpecialistDashboardUseCaseTest {

    @Test
    void shouldReturnDashboardSuccessfully() {
        SessionRepository sessionRepository = mock(SessionRepository.class);
        UserRepository userRepository = mock(UserRepository.class);
        SecurityUtils securityUtils = mock(SecurityUtils.class);

        // fixed clock for deterministic behavior
        LocalDateTime now = LocalDateTime.of(2026, Month.JULY, 15, 10, 0);
        Clock clock = Clock.fixed(now.atZone(ZoneId.systemDefault()).toInstant(), ZoneId.systemDefault());

        UUID specialistId = UUID.randomUUID();
        UUID patientId = UUID.randomUUID();

        when(securityUtils.getCurrentUserId()).thenReturn(specialistId);

        Session session = Session.restore(UUID.randomUUID(), specialistId, patientId,
                now.plusHours(1), now.plusHours(2), SessionStatus.CONFIRMED);

        when(sessionRepository.findNextSession(eq(specialistId), eq(now))).thenReturn(Optional.of(session));

        // user with telephone
        User patient = User.restore(patientId, "John Doe", "00000000000", "+55 (11) 99999-9999",
                "johndoe@example.com", "hash", UserRole.CLIENT, UserStatus.ACTIVE, Address.builder().street("s").city("c").state("st").zipCode("z").neighbourhood("n").build());

        when(userRepository.findById(patientId)).thenReturn(Optional.of(patient));

        // prepare expected counts for today and week
        LocalDate today = now.toLocalDate();
        LocalDateTime startOfDay = today.atStartOfDay();
        LocalDateTime weekStart = today.with(DayOfWeek.MONDAY).atStartOfDay();

        when(sessionRepository.countSessionsByStatus(eq(specialistId), any(), any(), any()))
                .thenAnswer(invocation -> {
                    @SuppressWarnings("unchecked")
                    var statuses = (java.util.Collection<SessionStatus>) invocation.getArgument(1);
                    LocalDateTime start = invocation.getArgument(2);
                    if (start.equals(startOfDay)) {
                        if (statuses.contains(SessionStatus.COMPLETED)) return 1L;
                        return 2L; // confirmed
                    }
                    if (start.equals(weekStart)) {
                        if (statuses.contains(SessionStatus.COMPLETED)) return 5L;
                        return 3L;
                    }
                    return 0L;
                });

        GetSpecialistDashboardUseCase useCase = new GetSpecialistDashboardUseCase(sessionRepository, userRepository, securityUtils, clock);

        DashboardResponse response = useCase.execute();

        assertNotNull(response);
        assertNotNull(response.nextAppointment());
        assertEquals(patient.getName(), response.nextAppointment().patientName());
        assertTrue(response.nextAppointment().whatsappUrl().contains("55"));

        assertNotNull(response.todaySummary());
        assertEquals(3L, response.todaySummary().totalAppointments());
        assertEquals(1L, response.todaySummary().completedAppointments());
        assertEquals(2L, response.todaySummary().remainingAppointments());

        assertNotNull(response.weekSummary());
        assertEquals(8L, response.weekSummary().totalAppointments());
        assertEquals(5L, response.weekSummary().completedAppointments());
    }

    @Test
    void shouldReturnNullWhenThereIsNoNextAppointment() {
        SessionRepository sessionRepository = mock(SessionRepository.class);
        UserRepository userRepository = mock(UserRepository.class);
        SecurityUtils securityUtils = mock(SecurityUtils.class);

        LocalDateTime now = LocalDateTime.of(2026, Month.JULY, 15, 10, 0);
        Clock clock = Clock.fixed(now.atZone(ZoneId.systemDefault()).toInstant(), ZoneId.systemDefault());

        UUID specialistId = UUID.randomUUID();
        when(securityUtils.getCurrentUserId()).thenReturn(specialistId);

        when(sessionRepository.findNextSession(eq(specialistId), eq(now))).thenReturn(Optional.empty());

        when(sessionRepository.countSessionsByStatus(any(), any(), any(), any())).thenReturn(0L);

        GetSpecialistDashboardUseCase useCase = new GetSpecialistDashboardUseCase(sessionRepository, userRepository, securityUtils, clock);

        var response = useCase.execute();

        assertNotNull(response);
        assertNull(response.nextAppointment());
    }

    @Test
    void shouldThrowWhenPatientDoesNotExist() {
        SessionRepository sessionRepository = mock(SessionRepository.class);
        UserRepository userRepository = mock(UserRepository.class);
        SecurityUtils securityUtils = mock(SecurityUtils.class);

        LocalDateTime now = LocalDateTime.of(2026, Month.JULY, 15, 10, 0);
        Clock clock = Clock.fixed(now.atZone(ZoneId.systemDefault()).toInstant(), ZoneId.systemDefault());

        UUID specialistId = UUID.randomUUID();
        UUID patientId = UUID.randomUUID();
        when(securityUtils.getCurrentUserId()).thenReturn(specialistId);

        Session session = Session.restore(UUID.randomUUID(), specialistId, patientId,
                now.plusHours(1), now.plusHours(2), SessionStatus.CONFIRMED);

        when(sessionRepository.findNextSession(eq(specialistId), eq(now))).thenReturn(Optional.of(session));
        when(userRepository.findById(patientId)).thenReturn(Optional.empty());

        GetSpecialistDashboardUseCase useCase = new GetSpecialistDashboardUseCase(sessionRepository, userRepository, securityUtils, clock);

        assertThrows(UserNotFoundException.class, useCase::execute);
    }

    @Test
    void shouldGenerateTodaySummaryCorrectly() {
        // re-use logic from shouldReturnDashboardSuccessfully but assert only today summary fields
        SessionRepository sessionRepository = mock(SessionRepository.class);
        UserRepository userRepository = mock(UserRepository.class);
        SecurityUtils securityUtils = mock(SecurityUtils.class);

        LocalDateTime now = LocalDateTime.of(2026, Month.JULY, 15, 10, 0);
        Clock clock = Clock.fixed(now.atZone(ZoneId.systemDefault()).toInstant(), ZoneId.systemDefault());

        UUID specialistId = UUID.randomUUID();
        when(securityUtils.getCurrentUserId()).thenReturn(specialistId);

        when(sessionRepository.findNextSession(eq(specialistId), eq(now))).thenReturn(Optional.empty());

        LocalDate today = now.toLocalDate();
        LocalDateTime startOfDay = today.atStartOfDay();

        when(sessionRepository.countSessionsByStatus(eq(specialistId), any(), eq(startOfDay), any()))
                .thenAnswer(invocation -> {
                    @SuppressWarnings("unchecked")
                    var statuses = (java.util.Collection<SessionStatus>) invocation.getArgument(1);
                    if (statuses.contains(SessionStatus.COMPLETED)) return 7L;
                    return 4L;
                });

        GetSpecialistDashboardUseCase useCase = new GetSpecialistDashboardUseCase(sessionRepository, userRepository, securityUtils, clock);

        var response = useCase.execute();

        assertNotNull(response.todaySummary());
        assertEquals(11L, response.todaySummary().totalAppointments());
        assertEquals(7L, response.todaySummary().completedAppointments());
        assertEquals(4L, response.todaySummary().remainingAppointments());
    }

    @Test
    void shouldGenerateWeekSummaryCorrectly() {
        SessionRepository sessionRepository = mock(SessionRepository.class);
        UserRepository userRepository = mock(UserRepository.class);
        SecurityUtils securityUtils = mock(SecurityUtils.class);

        LocalDateTime now = LocalDateTime.of(2026, Month.JULY, 15, 10, 0);
        Clock clock = Clock.fixed(now.atZone(ZoneId.systemDefault()).toInstant(), ZoneId.systemDefault());

        UUID specialistId = UUID.randomUUID();
        when(securityUtils.getCurrentUserId()).thenReturn(specialistId);

        when(sessionRepository.findNextSession(eq(specialistId), eq(now))).thenReturn(Optional.empty());

        LocalDate today = now.toLocalDate();
        LocalDateTime weekStart = today.with(DayOfWeek.MONDAY).atStartOfDay();

        when(sessionRepository.countSessionsByStatus(eq(specialistId), any(), eq(weekStart), any()))
                .thenAnswer(invocation -> {
                    @SuppressWarnings("unchecked")
                    var statuses = (java.util.Collection<SessionStatus>) invocation.getArgument(1);
                    if (statuses.contains(SessionStatus.COMPLETED)) return 9L;
                    return 0L;
                });

        GetSpecialistDashboardUseCase useCase = new GetSpecialistDashboardUseCase(sessionRepository, userRepository, securityUtils, clock);

        var response = useCase.execute();

        assertNotNull(response.weekSummary());
        assertEquals(9L, response.weekSummary().totalAppointments());
        assertEquals(9L, response.weekSummary().completedAppointments());
    }

    @Test
    void shouldReturnNullWhatsappWhenPhoneIsNull() {
        SessionRepository sessionRepository = mock(SessionRepository.class);
        UserRepository userRepository = mock(UserRepository.class);
        SecurityUtils securityUtils = mock(SecurityUtils.class);

        LocalDateTime now = LocalDateTime.of(2026, Month.JULY, 15, 10, 0);
        Clock clock = Clock.fixed(now.atZone(ZoneId.systemDefault()).toInstant(), ZoneId.systemDefault());

        UUID specialistId = UUID.randomUUID();
        UUID patientId = UUID.randomUUID();
        when(securityUtils.getCurrentUserId()).thenReturn(specialistId);

        Session session = Session.restore(UUID.randomUUID(), specialistId, patientId,
                now.plusHours(1), now.plusHours(2), SessionStatus.CONFIRMED);

        when(sessionRepository.findNextSession(eq(specialistId), eq(now))).thenReturn(Optional.of(session));

        User patient = User.restore(patientId, "Jane Doe", "11111111111", null,
                "jane@example.com", "hash", UserRole.CLIENT, UserStatus.ACTIVE, Address.builder().street("s").city("c").state("st").zipCode("z").neighbourhood("n").build());

        when(userRepository.findById(patientId)).thenReturn(Optional.of(patient));

        when(sessionRepository.countSessionsByStatus(any(), any(), any(), any())).thenReturn(0L);

        GetSpecialistDashboardUseCase useCase = new GetSpecialistDashboardUseCase(sessionRepository, userRepository, securityUtils, clock);

        var response = useCase.execute();

        assertNotNull(response.nextAppointment());
        assertNull(response.nextAppointment().whatsappUrl());
    }
}