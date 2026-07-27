package com.soupulsar.application.usecase.specialist;

import com.soupulsar.application.specialist.dashboard.DashboardResponse;
import com.soupulsar.application.specialist.dashboard.GetSpecialistDashboardUseCase;
import com.soupulsar.application.specialist.shared.WhatsAppLinkGenerator;
import com.soupulsar.application.specialist.shared.daterange.DateRangeFactory;
import com.soupulsar.application.specialist.shared.summary.SessionStatisticsCalculator;
import com.soupulsar.application.specialist.shared.summary.SessionSummary;
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
import java.time.LocalDateTime;
import java.time.Month;
import java.time.ZoneId;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
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
        DateRangeFactory dateRangeFactory = mock(DateRangeFactory.class);
        SessionStatisticsCalculator sessionStatisticsCalculator = mock(SessionStatisticsCalculator.class);
        WhatsAppLinkGenerator whatsAppLinkGenerator = mock(WhatsAppLinkGenerator.class);

        // fixed clock for deterministic behavior
        LocalDateTime now = LocalDateTime.of(2026, Month.JULY, 15, 10, 0);
        Clock clock = Clock.fixed(now.atZone(ZoneId.systemDefault()).toInstant(), ZoneId.systemDefault());

        UUID specialistId = UUID.randomUUID();
        UUID patientId = UUID.randomUUID();

        when(securityUtils.getCurrentUserId()).thenReturn(specialistId);

        Session session = Session.restore(UUID.randomUUID(), specialistId, patientId,
                now.plusHours(1), now.plusHours(2), SessionStatus.CONFIRMED);

        when(sessionRepository.findNextSession(specialistId, now)).thenReturn(Optional.of(session));

        // user with telephone
        User patient = User.restore(patientId, "John Doe", "00000000000", "+55 (11) 99999-9999",
                "johndoe@example.com", "hash", UserRole.CLIENT, UserStatus.ACTIVE, Address.builder().street("s").city("c").state("st").zipCode("z").neighbourhood("n").build());

        when(userRepository.findById(patientId)).thenReturn(Optional.of(patient));

        String whatsappUrl = "https://wa.me/5511999999999";
        when(whatsAppLinkGenerator.generate("+55 (11) 99999-9999")).thenReturn(whatsappUrl);

        SessionSummary todaySummary = new SessionSummary(1L, 2L);
        SessionSummary weekSummary = new SessionSummary(5L, 3L);

        when(sessionStatisticsCalculator.calculate(eq(specialistId), any())).thenReturn(todaySummary).thenReturn(weekSummary);

        GetSpecialistDashboardUseCase useCase = new GetSpecialistDashboardUseCase(sessionRepository, userRepository, securityUtils, dateRangeFactory, sessionStatisticsCalculator, whatsAppLinkGenerator, clock);

        DashboardResponse response = useCase.execute();

        assertNotNull(response);
        assertNotNull(response.nextAppointment());
        assertEquals(patient.getName(), response.nextAppointment().patientName());
        assertEquals(whatsappUrl, response.nextAppointment().whatsappUrl());

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
        DateRangeFactory dateRangeFactory = mock(DateRangeFactory.class);
        SessionStatisticsCalculator sessionStatisticsCalculator = mock(SessionStatisticsCalculator.class);
        WhatsAppLinkGenerator whatsAppLinkGenerator = mock(WhatsAppLinkGenerator.class);

        LocalDateTime now = LocalDateTime.of(2026, Month.JULY, 15, 10, 0);
        Clock clock = Clock.fixed(now.atZone(ZoneId.systemDefault()).toInstant(), ZoneId.systemDefault());

        UUID specialistId = UUID.randomUUID();
        when(securityUtils.getCurrentUserId()).thenReturn(specialistId);

        when(sessionRepository.findNextSession(specialistId, now)).thenReturn(Optional.empty());

        SessionSummary emptySummary = new SessionSummary(0L, 0L);
        when(sessionStatisticsCalculator.calculate(any(), any())).thenReturn(emptySummary);

        GetSpecialistDashboardUseCase useCase = new GetSpecialistDashboardUseCase(sessionRepository, userRepository, securityUtils, dateRangeFactory, sessionStatisticsCalculator, whatsAppLinkGenerator, clock);

        var response = useCase.execute();

        assertNotNull(response);
        assertNull(response.nextAppointment());
    }

    @Test
    void shouldThrowWhenPatientDoesNotExist() {
        SessionRepository sessionRepository = mock(SessionRepository.class);
        UserRepository userRepository = mock(UserRepository.class);
        SecurityUtils securityUtils = mock(SecurityUtils.class);
        DateRangeFactory dateRangeFactory = mock(DateRangeFactory.class);
        SessionStatisticsCalculator sessionStatisticsCalculator = mock(SessionStatisticsCalculator.class);
        WhatsAppLinkGenerator whatsAppLinkGenerator = mock(WhatsAppLinkGenerator.class);

        LocalDateTime now = LocalDateTime.of(2026, Month.JULY, 15, 10, 0);
        Clock clock = Clock.fixed(now.atZone(ZoneId.systemDefault()).toInstant(), ZoneId.systemDefault());

        UUID specialistId = UUID.randomUUID();
        UUID patientId = UUID.randomUUID();
        when(securityUtils.getCurrentUserId()).thenReturn(specialistId);

        Session session = Session.restore(UUID.randomUUID(), specialistId, patientId,
                now.plusHours(1), now.plusHours(2), SessionStatus.CONFIRMED);

        when(sessionRepository.findNextSession(specialistId, now)).thenReturn(Optional.of(session));
        when(userRepository.findById(patientId)).thenReturn(Optional.empty());

        GetSpecialistDashboardUseCase useCase = new GetSpecialistDashboardUseCase(sessionRepository, userRepository, securityUtils, dateRangeFactory, sessionStatisticsCalculator, whatsAppLinkGenerator, clock);

        assertThrows(UserNotFoundException.class, useCase::execute);
    }

    @Test
    void shouldGenerateTodaySummaryCorrectly() {
        SessionRepository sessionRepository = mock(SessionRepository.class);
        UserRepository userRepository = mock(UserRepository.class);
        SecurityUtils securityUtils = mock(SecurityUtils.class);
        DateRangeFactory dateRangeFactory = mock(DateRangeFactory.class);
        SessionStatisticsCalculator sessionStatisticsCalculator = mock(SessionStatisticsCalculator.class);
        WhatsAppLinkGenerator whatsAppLinkGenerator = mock(WhatsAppLinkGenerator.class);

        LocalDateTime now = LocalDateTime.of(2026, Month.JULY, 15, 10, 0);
        Clock clock = Clock.fixed(now.atZone(ZoneId.systemDefault()).toInstant(), ZoneId.systemDefault());

        UUID specialistId = UUID.randomUUID();
        when(securityUtils.getCurrentUserId()).thenReturn(specialistId);

        when(sessionRepository.findNextSession(specialistId, now)).thenReturn(Optional.empty());

        SessionSummary todaySummary = new SessionSummary(7L, 4L);
        SessionSummary weekSummary = new SessionSummary(0L, 0L);

        when(sessionStatisticsCalculator.calculate(any(), any())).thenReturn(todaySummary).thenReturn(weekSummary);

        GetSpecialistDashboardUseCase useCase = new GetSpecialistDashboardUseCase(sessionRepository, userRepository, securityUtils, dateRangeFactory, sessionStatisticsCalculator, whatsAppLinkGenerator, clock);

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
        DateRangeFactory dateRangeFactory = mock(DateRangeFactory.class);
        SessionStatisticsCalculator sessionStatisticsCalculator = mock(SessionStatisticsCalculator.class);
        WhatsAppLinkGenerator whatsAppLinkGenerator = mock(WhatsAppLinkGenerator.class);

        LocalDateTime now = LocalDateTime.of(2026, Month.JULY, 15, 10, 0);
        Clock clock = Clock.fixed(now.atZone(ZoneId.systemDefault()).toInstant(), ZoneId.systemDefault());

        UUID specialistId = UUID.randomUUID();
        when(securityUtils.getCurrentUserId()).thenReturn(specialistId);

        when(sessionRepository.findNextSession(specialistId, now)).thenReturn(Optional.empty());

        SessionSummary todaySummary = new SessionSummary(0L, 0L);
        SessionSummary weekSummary = new SessionSummary(9L, 0L);

        when(sessionStatisticsCalculator.calculate(any(), any())).thenReturn(todaySummary).thenReturn(weekSummary);

        GetSpecialistDashboardUseCase useCase = new GetSpecialistDashboardUseCase(sessionRepository, userRepository, securityUtils, dateRangeFactory, sessionStatisticsCalculator, whatsAppLinkGenerator, clock);

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
        DateRangeFactory dateRangeFactory = mock(DateRangeFactory.class);
        SessionStatisticsCalculator sessionStatisticsCalculator = mock(SessionStatisticsCalculator.class);
        WhatsAppLinkGenerator whatsAppLinkGenerator = mock(WhatsAppLinkGenerator.class);

        LocalDateTime now = LocalDateTime.of(2026, Month.JULY, 15, 10, 0);
        Clock clock = Clock.fixed(now.atZone(ZoneId.systemDefault()).toInstant(), ZoneId.systemDefault());

        UUID specialistId = UUID.randomUUID();
        UUID patientId = UUID.randomUUID();
        when(securityUtils.getCurrentUserId()).thenReturn(specialistId);

        Session session = Session.restore(UUID.randomUUID(), specialistId, patientId,
                now.plusHours(1), now.plusHours(2), SessionStatus.CONFIRMED);

        when(sessionRepository.findNextSession(specialistId, now)).thenReturn(Optional.of(session));

        User patient = User.restore(patientId, "Jane Doe", "11111111111", null,
                "jane@example.com", "hash", UserRole.CLIENT, UserStatus.ACTIVE, Address.builder().street("s").city("c").state("st").zipCode("z").neighbourhood("n").build());

        when(userRepository.findById(patientId)).thenReturn(Optional.of(patient));
        when(whatsAppLinkGenerator.generate(null)).thenReturn(null);

        SessionSummary emptySummary = new SessionSummary(0L, 0L);
        when(sessionStatisticsCalculator.calculate(any(), any())).thenReturn(emptySummary);

        GetSpecialistDashboardUseCase useCase = new GetSpecialistDashboardUseCase(sessionRepository, userRepository, securityUtils, dateRangeFactory, sessionStatisticsCalculator, whatsAppLinkGenerator, clock);

        var response = useCase.execute();

        assertNotNull(response.nextAppointment());
        assertNull(response.nextAppointment().whatsappUrl());
    }
}