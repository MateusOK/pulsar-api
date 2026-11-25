package com.soupulsar.application.client;

import com.soupulsar.application.utils.SecurityUtils;
import com.soupulsar.domain.exceptions.UserNotFoundException;
import com.soupulsar.domain.model.enums.SessionStatus;
import com.soupulsar.domain.model.enums.SpecialistType;
import com.soupulsar.domain.model.enums.UserRole;
import com.soupulsar.domain.model.enums.UserStatus;
import com.soupulsar.domain.model.session.Session;
import com.soupulsar.domain.model.specialist.SpecialistProfile;
import com.soupulsar.domain.model.user.User;
import com.soupulsar.domain.model.vo.Address;
import com.soupulsar.domain.model.vo.Money;
import com.soupulsar.domain.model.vo.Presentation;
import com.soupulsar.domain.model.vo.RegistrationNumber;
import com.soupulsar.domain.repository.SessionRepository;
import com.soupulsar.domain.repository.SpecialistProfileRepository;
import com.soupulsar.domain.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.AuthenticationException;

import java.math.BigDecimal;
import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

class GetClientSessionsUseCaseTest {

    private static final ZoneId SAO_PAULO_ZONE = ZoneId.of("America/Sao_Paulo");

    private Clock clock;
    private LocalDateTime now;
    private SecurityUtils securityUtils;
    private SessionRepository sessionRepository;
    private UserRepository userRepository;
    private SpecialistProfileRepository specialistProfileRepository;
    private GetClientSessionsUseCase useCase;

    @BeforeEach
    void setUp() {
        now = LocalDateTime.of(2026, 7, 15, 10, 0);
        clock = Clock.fixed(now.atZone(SAO_PAULO_ZONE).toInstant(), SAO_PAULO_ZONE);
        securityUtils = mock(SecurityUtils.class);
        sessionRepository = mock(SessionRepository.class);
        userRepository = mock(UserRepository.class);
        specialistProfileRepository = mock(SpecialistProfileRepository.class);

        useCase = new GetClientSessionsUseCase(
                clock,
                securityUtils,
                sessionRepository,
                userRepository,
                specialistProfileRepository
        );
    }

    @Test
    void shouldReturnBothFutureSessionsAndHistoryForAuthenticatedUser() {
        UUID clientId = UUID.randomUUID();
        UUID specialist1Id = UUID.randomUUID();
        UUID specialist2Id = UUID.randomUUID();

        Session pastSession = Session.restore(
                UUID.randomUUID(), specialist1Id, clientId,
                now.minusDays(3), now.minusDays(3).plusMinutes(50), SessionStatus.COMPLETED
        );
        Session futureSession = Session.restore(
                UUID.randomUUID(), specialist2Id, clientId,
                now.plusDays(2), now.plusDays(2).plusMinutes(50), SessionStatus.CONFIRMED
        );

        User specialist1 = buildSpecialistUser(specialist1Id, "Dra. Ana Silva");
        User specialist2 = buildSpecialistUser(specialist2Id, "Dr. Bruno Costa");
        SpecialistProfile profile1 = buildSpecialistProfile(specialist1Id, "CRP-06/12345", SpecialistType.PSICOLOGO);
        SpecialistProfile profile2 = buildSpecialistProfile(specialist2Id, "CRM-SP/98765", SpecialistType.PSIQUIATRA);

        when(securityUtils.getCurrentUserId()).thenReturn(clientId);
        when(sessionRepository.findByClientId(clientId)).thenReturn(List.of(pastSession, futureSession));
        when(userRepository.findAllByUserId(List.of(specialist1Id, specialist2Id)))
                .thenReturn(Map.of(specialist1Id, specialist1, specialist2Id, specialist2));
        when(specialistProfileRepository.findAllByUserId(List.of(specialist1Id, specialist2Id)))
                .thenReturn(Map.of(specialist1Id, profile1, specialist2Id, profile2));

        ClientSessionsResponse response = useCase.execute();

        assertNotNull(response);
        assertEquals(1, response.futureSessions().size());
        assertEquals(1, response.history().size());

        ClientSessionResponse futureItem = response.futureSessions().getFirst();
        assertEquals(futureSession.getSessionId(), futureItem.sessionId());
        assertEquals(futureSession.getStartAt(), futureItem.startsAt());
        assertEquals(futureSession.getEndAt(), futureItem.endsAt());
        assertEquals(SessionStatus.CONFIRMED, futureItem.status());
        assertEquals(specialist2Id, futureItem.specialistId());
        assertEquals("Dr. Bruno Costa", futureItem.specialistName());
        assertEquals(new RegistrationNumber("CRM-SP/98765"), futureItem.registrationNumber());
        assertEquals(SpecialistType.PSIQUIATRA, futureItem.specialistType());

        ClientSessionResponse historyItem = response.history().getFirst();
        assertEquals(pastSession.getSessionId(), historyItem.sessionId());
        assertEquals(pastSession.getStartAt(), historyItem.startsAt());
        assertEquals(pastSession.getEndAt(), historyItem.endsAt());
        assertEquals(SessionStatus.COMPLETED, historyItem.status());
        assertEquals(specialist1Id, historyItem.specialistId());
        assertEquals("Dra. Ana Silva", historyItem.specialistName());
        assertEquals(new RegistrationNumber("CRP-06/12345"), historyItem.registrationNumber());
        assertEquals(SpecialistType.PSICOLOGO, historyItem.specialistType());

        verify(securityUtils).getCurrentUserId();
        verify(sessionRepository).findByClientId(clientId);
    }

    @Test
    void shouldReturnOnlyFutureSessionsWhenClientHasNoHistory() {
        UUID clientId = UUID.randomUUID();
        UUID specialistId = UUID.randomUUID();

        Session laterFuture = Session.restore(
                UUID.randomUUID(), specialistId, clientId,
                now.plusDays(5), now.plusDays(5).plusMinutes(50), SessionStatus.AWAITING_PAYMENT
        );
        Session soonerFuture = Session.restore(
                UUID.randomUUID(), specialistId, clientId,
                now.plusDays(1), now.plusDays(1).plusMinutes(50), SessionStatus.CONFIRMED
        );

        User specialist = buildSpecialistUser(specialistId, "Dra. Carla Mendes");
        SpecialistProfile profile = buildSpecialistProfile(specialistId, "CRN-3/54321", SpecialistType.NUTRICIONISTA);

        when(securityUtils.getCurrentUserId()).thenReturn(clientId);
        when(sessionRepository.findByClientId(clientId)).thenReturn(List.of(laterFuture, soonerFuture));
        when(userRepository.findAllByUserId(List.of(specialistId))).thenReturn(Map.of(specialistId, specialist));
        when(specialistProfileRepository.findAllByUserId(List.of(specialistId))).thenReturn(Map.of(specialistId, profile));

        ClientSessionsResponse response = useCase.execute();

        assertEquals(2, response.futureSessions().size());
        assertTrue(response.history().isEmpty());
        assertEquals(soonerFuture.getSessionId(), response.futureSessions().get(0).sessionId());
        assertEquals(laterFuture.getSessionId(), response.futureSessions().get(1).sessionId());
    }

    @Test
    void shouldReturnOnlyHistoryWhenClientHasNoFutureSessions() {
        UUID clientId = UUID.randomUUID();
        UUID specialistId = UUID.randomUUID();

        Session olderPast = Session.restore(
                UUID.randomUUID(), specialistId, clientId,
                now.minusDays(10), now.minusDays(10).plusMinutes(50), SessionStatus.COMPLETED
        );
        Session recentPast = Session.restore(
                UUID.randomUUID(), specialistId, clientId,
                now.minusHours(2), now.minusHours(1), SessionStatus.CANCELLED
        );

        User specialist = buildSpecialistUser(specialistId, "Dr. Diego Lima");
        SpecialistProfile profile = buildSpecialistProfile(specialistId, "CREF-04/11111", SpecialistType.EDUCADOR_FISICO);

        when(securityUtils.getCurrentUserId()).thenReturn(clientId);
        when(sessionRepository.findByClientId(clientId)).thenReturn(List.of(olderPast, recentPast));
        when(userRepository.findAllByUserId(List.of(specialistId))).thenReturn(Map.of(specialistId, specialist));
        when(specialistProfileRepository.findAllByUserId(List.of(specialistId))).thenReturn(Map.of(specialistId, profile));

        ClientSessionsResponse response = useCase.execute();

        assertTrue(response.futureSessions().isEmpty());
        assertEquals(2, response.history().size());
        assertEquals(recentPast.getSessionId(), response.history().get(0).sessionId());
        assertEquals(olderPast.getSessionId(), response.history().get(1).sessionId());
    }

    @Test
    void shouldReturnEmptyListsWhenClientHasNoBookedSessions() {
        UUID clientId = UUID.randomUUID();

        when(securityUtils.getCurrentUserId()).thenReturn(clientId);
        when(sessionRepository.findByClientId(clientId)).thenReturn(List.of());

        ClientSessionsResponse response = useCase.execute();

        assertNotNull(response);
        assertTrue(response.futureSessions().isEmpty());
        assertTrue(response.history().isEmpty());
        verifyNoInteractions(userRepository, specialistProfileRepository);
    }

    @Test
    void shouldClassifySessionsCorrectlyUsingClockAndTimezone() {
        // 2026-07-15T13:00:00Z corresponds to 2026-07-15T10:00:00 in America/Sao_Paulo (UTC-3)
        Clock saoPauloClock = Clock.fixed(Instant.parse("2026-07-15T13:00:00Z"), SAO_PAULO_ZONE);
        useCase = new GetClientSessionsUseCase(
                saoPauloClock,
                securityUtils,
                sessionRepository,
                userRepository,
                specialistProfileRepository
        );

        UUID clientId = UUID.randomUUID();
        UUID specialistId = UUID.randomUUID();

        LocalDateTime saoPauloNow = LocalDateTime.of(2026, 7, 15, 10, 0);
        Session oneMinuteAgo = Session.restore(
                UUID.randomUUID(), specialistId, clientId,
                saoPauloNow.minusMinutes(1), saoPauloNow.plusMinutes(49), SessionStatus.CONFIRMED
        );
        Session exactNow = Session.restore(
                UUID.randomUUID(), specialistId, clientId,
                saoPauloNow, saoPauloNow.plusMinutes(50), SessionStatus.CONFIRMED
        );
        Session oneMinuteAhead = Session.restore(
                UUID.randomUUID(), specialistId, clientId,
                saoPauloNow.plusMinutes(1), saoPauloNow.plusMinutes(51), SessionStatus.CONFIRMED
        );

        User specialist = buildSpecialistUser(specialistId, "Dra. Elena Souza");
        SpecialistProfile profile = buildSpecialistProfile(specialistId, "CRP-01/22222", SpecialistType.TERAPEUTA);

        when(securityUtils.getCurrentUserId()).thenReturn(clientId);
        when(sessionRepository.findByClientId(clientId)).thenReturn(List.of(oneMinuteAgo, exactNow, oneMinuteAhead));
        when(userRepository.findAllByUserId(List.of(specialistId))).thenReturn(Map.of(specialistId, specialist));
        when(specialistProfileRepository.findAllByUserId(List.of(specialistId))).thenReturn(Map.of(specialistId, profile));

        ClientSessionsResponse response = useCase.execute();

        assertEquals(2, response.futureSessions().size());
        assertEquals(exactNow.getSessionId(), response.futureSessions().get(0).sessionId());
        assertEquals(oneMinuteAhead.getSessionId(), response.futureSessions().get(1).sessionId());

        assertEquals(1, response.history().size());
        assertEquals(oneMinuteAgo.getSessionId(), response.history().getFirst().sessionId());
    }

    @Test
    void shouldIdentifyCurrentUserThroughSecurityUtils() {
        UUID authenticatedClientId = UUID.randomUUID();
        when(securityUtils.getCurrentUserId()).thenReturn(authenticatedClientId);
        when(sessionRepository.findByClientId(authenticatedClientId)).thenReturn(List.of());

        useCase.execute();

        verify(securityUtils).getCurrentUserId();
        verify(sessionRepository).findByClientId(authenticatedClientId);
    }

    @Test
    void shouldPropagateAuthenticationExceptionWhenNoUserIsAuthenticated() {
        when(securityUtils.getCurrentUserId()).thenThrow(new AuthenticationException("No authenticated user found") {});

        assertThrows(AuthenticationException.class, () -> useCase.execute());
        verifyNoInteractions(sessionRepository, userRepository, specialistProfileRepository);
    }

    @Test
    void shouldThrowWhenSpecialistUserIsMissing() {
        UUID clientId = UUID.randomUUID();
        UUID specialistId = UUID.randomUUID();
        Session session = Session.restore(
                UUID.randomUUID(), specialistId, clientId,
                now.plusDays(1), now.plusDays(1).plusMinutes(50), SessionStatus.CONFIRMED
        );

        when(securityUtils.getCurrentUserId()).thenReturn(clientId);
        when(sessionRepository.findByClientId(clientId)).thenReturn(List.of(session));
        when(userRepository.findAllByUserId(List.of(specialistId))).thenReturn(Map.of());
        when(specialistProfileRepository.findAllByUserId(List.of(specialistId)))
                .thenReturn(Map.of(specialistId, buildSpecialistProfile(specialistId, "CRP-06/12345", SpecialistType.PSICOLOGO)));

        assertThrows(UserNotFoundException.class, () -> useCase.execute());
    }

    @Test
    void shouldThrowWhenSpecialistProfileIsMissing() {
        UUID clientId = UUID.randomUUID();
        UUID specialistId = UUID.randomUUID();
        Session session = Session.restore(
                UUID.randomUUID(), specialistId, clientId,
                now.plusDays(1), now.plusDays(1).plusMinutes(50), SessionStatus.CONFIRMED
        );

        when(securityUtils.getCurrentUserId()).thenReturn(clientId);
        when(sessionRepository.findByClientId(clientId)).thenReturn(List.of(session));
        when(userRepository.findAllByUserId(List.of(specialistId)))
                .thenReturn(Map.of(specialistId, buildSpecialistUser(specialistId, "Dra. Ana Silva")));
        when(specialistProfileRepository.findAllByUserId(List.of(specialistId))).thenReturn(Map.of());

        assertThrows(UserNotFoundException.class, () -> useCase.execute());
    }

    private User buildSpecialistUser(UUID userId, String name) {
        return User.restore(
                userId,
                name,
                "12345678900",
                "+55 11 99999-9999",
                "specialist@example.com",
                "hashed-password",
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
    }

    private SpecialistProfile buildSpecialistProfile(UUID userId, String registrationNumber, SpecialistType specialistType) {
        return SpecialistProfile.builder()
                .profileId(UUID.randomUUID())
                .userId(userId)
                .registrationNumber(new RegistrationNumber(registrationNumber))
                .specialistType(specialistType)
                .sessionPrice(new Money(new BigDecimal("150.00")))
                .presentation(Presentation.builder().about("Bio").build())
                .build();
    }
}
