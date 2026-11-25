package com.soupulsar.application.client;

import com.soupulsar.application.utils.SecurityUtils;
import com.soupulsar.domain.exceptions.UserNotFoundException;
import com.soupulsar.domain.model.enums.SessionStatus;
import com.soupulsar.domain.model.enums.UserRole;
import com.soupulsar.domain.model.enums.UserStatus;
import com.soupulsar.domain.model.session.Session;
import com.soupulsar.domain.model.user.User;
import com.soupulsar.domain.model.vo.Address;
import com.soupulsar.domain.repository.SessionRepository;
import com.soupulsar.domain.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.AuthenticationException;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

class GetClientNextAppointmentUseCaseTest {

    private static final ZoneId SAO_PAULO_ZONE = ZoneId.of("America/Sao_Paulo");

    private Clock clock;
    private LocalDateTime now;
    private SecurityUtils securityUtils;
    private UserRepository userRepository;
    private SessionRepository sessionRepository;
    private GetClientNextAppointmentUseCase useCase;

    @BeforeEach
    void setUp() {
        now = LocalDateTime.of(2026, 7, 15, 10, 0);
        clock = Clock.fixed(now.atZone(SAO_PAULO_ZONE).toInstant(), SAO_PAULO_ZONE);
        securityUtils = mock(SecurityUtils.class);
        userRepository = mock(UserRepository.class);
        sessionRepository = mock(SessionRepository.class);

        useCase = new GetClientNextAppointmentUseCase(clock, securityUtils, userRepository, sessionRepository);
    }

    @Test
    void shouldReturnNextAppointmentSuccessfully() {
        UUID clientId = UUID.randomUUID();
        UUID specialistId = UUID.randomUUID();
        UUID sessionId = UUID.randomUUID();

        Session nextSession = Session.restore(
                sessionId, specialistId, clientId,
                now.plusHours(2), now.plusHours(3), SessionStatus.CONFIRMED
        );
        User specialist = User.restore(
                specialistId,
                "Dra. Ana Silva",
                "12345678900",
                "+55 11 99999-9999",
                "ana@example.com",
                "hash",
                UserRole.SPECIALIST,
                UserStatus.ACTIVE,
                Address.builder()
                        .street("Rua A")
                        .city("Sao Paulo")
                        .state("SP")
                        .zipCode("01000-000")
                        .neighbourhood("Centro")
                        .build()
        );

        when(securityUtils.getCurrentUserId()).thenReturn(clientId);
        when(sessionRepository.findNextClientSession(clientId, now)).thenReturn(Optional.of(nextSession));
        when(userRepository.findById(specialistId)).thenReturn(Optional.of(specialist));

        HomeResponse response = useCase.execute();

        assertNotNull(response);
        assertNotNull(response.nextAppointment());
        assertEquals(sessionId, response.nextAppointment().id());
        assertEquals(nextSession.getStartAt(), response.nextAppointment().startsAt());
        assertEquals(nextSession.getEndAt(), response.nextAppointment().endsAt());
        assertEquals("Dra. Ana Silva", response.nextAppointment().specialistName());

        verify(securityUtils).getCurrentUserId();
        verify(sessionRepository).findNextClientSession(clientId, now);
        verify(userRepository).findById(specialistId);
    }

    @Test
    void shouldReturnNullNextAppointmentWhenNoSessionExists() {
        UUID clientId = UUID.randomUUID();

        when(securityUtils.getCurrentUserId()).thenReturn(clientId);
        when(sessionRepository.findNextClientSession(clientId, now)).thenReturn(Optional.empty());

        HomeResponse response = useCase.execute();

        assertNotNull(response);
        assertNull(response.nextAppointment());
        verify(sessionRepository).findNextClientSession(clientId, now);
        verifyNoInteractions(userRepository);
    }

    @Test
    void shouldQueryUsingClockTimezone() {
        // 2026-07-15T13:00:00Z == 2026-07-15T10:00:00 in America/Sao_Paulo (UTC-3)
        Clock saoPauloClock = Clock.fixed(Instant.parse("2026-07-15T13:00:00Z"), SAO_PAULO_ZONE);
        useCase = new GetClientNextAppointmentUseCase(saoPauloClock, securityUtils, userRepository, sessionRepository);

        UUID clientId = UUID.randomUUID();
        LocalDateTime expectedNow = LocalDateTime.of(2026, 7, 15, 10, 0);

        when(securityUtils.getCurrentUserId()).thenReturn(clientId);
        when(sessionRepository.findNextClientSession(clientId, expectedNow)).thenReturn(Optional.empty());

        useCase.execute();

        verify(sessionRepository).findNextClientSession(clientId, expectedNow);
    }

    @Test
    void shouldThrowWhenSpecialistUserNotFound() {
        UUID clientId = UUID.randomUUID();
        UUID specialistId = UUID.randomUUID();
        Session nextSession = Session.restore(
                UUID.randomUUID(), specialistId, clientId,
                now.plusHours(1), now.plusHours(2), SessionStatus.CONFIRMED
        );

        when(securityUtils.getCurrentUserId()).thenReturn(clientId);
        when(sessionRepository.findNextClientSession(clientId, now)).thenReturn(Optional.of(nextSession));
        when(userRepository.findById(specialistId)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> useCase.execute());
    }

    @Test
    void shouldPropagateAuthenticationExceptionWhenUnauthenticated() {
        when(securityUtils.getCurrentUserId()).thenThrow(new AuthenticationException("No authenticated user found") {});

        assertThrows(AuthenticationException.class, () -> useCase.execute());
        verifyNoInteractions(sessionRepository, userRepository);
    }
}
