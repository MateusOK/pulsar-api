package com.soupulsar.application.usecase.specialist;

import com.soupulsar.application.specialist.calendar.GetSessionDetailsUseCase;
import com.soupulsar.application.specialist.calendar.SessionDetailsResponse;
import com.soupulsar.application.specialist.shared.WhatsAppLinkGenerator;
import com.soupulsar.application.utils.SecurityUtils;
import com.soupulsar.domain.exceptions.SessionNotFoundException;
import com.soupulsar.domain.exceptions.UserNotFoundException;
import com.soupulsar.domain.model.enums.SessionStatus;
import com.soupulsar.domain.model.enums.UserRole;
import com.soupulsar.domain.model.enums.UserStatus;
import com.soupulsar.domain.model.session.Session;
import com.soupulsar.domain.model.user.User;
import com.soupulsar.domain.model.vo.Address;
import com.soupulsar.domain.repository.SessionRepository;
import com.soupulsar.domain.repository.UserRepository;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class GetSessionDetailsUseCaseTest {

    @Test
    void shouldReturnSessionDetailsSuccessfully() {
        SecurityUtils securityUtils = mock(SecurityUtils.class);
        SessionRepository sessionRepository = mock(SessionRepository.class);
        UserRepository userRepository = mock(UserRepository.class);
        WhatsAppLinkGenerator whatsAppLinkGenerator = mock(WhatsAppLinkGenerator.class);

        LocalDateTime now = LocalDateTime.of(2026, Month.JULY, 22, 10, 0);

        UUID specialistId = UUID.randomUUID();
        UUID clientId = UUID.randomUUID();
        UUID sessionId = UUID.randomUUID();

        when(securityUtils.getCurrentUserId()).thenReturn(specialistId);

        Session session = Session.restore(sessionId, specialistId, clientId, now.plusHours(1), now.plusHours(2), SessionStatus.CONFIRMED);

        when(sessionRepository.findBySessionIdAndSpecialistId(sessionId, specialistId)).thenReturn(Optional.of(session));

        User client = User.restore(clientId, "Bob", "00000000000", "+55 11 99999-9999", "bob@example.com", "hash", UserRole.CLIENT, UserStatus.ACTIVE, Address.builder().street("s").city("c").state("st").zipCode("z").neighbourhood("n").build());

        when(userRepository.findById(clientId)).thenReturn(Optional.of(client));

        String whatsapp = "https://wa.me/5511999999999";
        when(whatsAppLinkGenerator.generate(client.getTelephone())).thenReturn(whatsapp);

        GetSessionDetailsUseCase useCase = new GetSessionDetailsUseCase(securityUtils, sessionRepository, userRepository, whatsAppLinkGenerator);

        SessionDetailsResponse response = useCase.execute(sessionId);

        assertNotNull(response);
        assertEquals(sessionId, response.sessionId());
        assertEquals(clientId, response.clientId());
        assertEquals(client.getName(), response.clientName());
        assertEquals(client.getEmail(), response.clientEmail());
        assertEquals(client.getTelephone(), response.clientPhone());
        assertEquals(whatsapp, response.whatsappUrl());
    }

    @Test
    void shouldThrowWhenSessionNotFound() {
        SecurityUtils securityUtils = mock(SecurityUtils.class);
        SessionRepository sessionRepository = mock(SessionRepository.class);
        UserRepository userRepository = mock(UserRepository.class);
        WhatsAppLinkGenerator whatsAppLinkGenerator = mock(WhatsAppLinkGenerator.class);

        UUID specialistId = UUID.randomUUID();
        UUID sessionId = UUID.randomUUID();

        when(securityUtils.getCurrentUserId()).thenReturn(specialistId);

        when(sessionRepository.findBySessionIdAndSpecialistId(sessionId, specialistId)).thenReturn(Optional.empty());

        GetSessionDetailsUseCase useCase = new GetSessionDetailsUseCase(securityUtils, sessionRepository, userRepository, whatsAppLinkGenerator);

        assertThrows(SessionNotFoundException.class, () -> useCase.execute(sessionId));
    }

    @Test
    void shouldThrowWhenClientNotFound() {
        SecurityUtils securityUtils = mock(SecurityUtils.class);
        SessionRepository sessionRepository = mock(SessionRepository.class);
        UserRepository userRepository = mock(UserRepository.class);
        WhatsAppLinkGenerator whatsAppLinkGenerator = mock(WhatsAppLinkGenerator.class);

        LocalDateTime now = LocalDateTime.of(2026, Month.JULY, 22, 10, 0);

        UUID specialistId = UUID.randomUUID();
        UUID clientId = UUID.randomUUID();
        UUID sessionId = UUID.randomUUID();

        when(securityUtils.getCurrentUserId()).thenReturn(specialistId);

        Session session = Session.restore(sessionId, specialistId, clientId, now.plusHours(1), now.plusHours(2), SessionStatus.CONFIRMED);
        when(sessionRepository.findBySessionIdAndSpecialistId(sessionId, specialistId)).thenReturn(Optional.of(session));

        when(userRepository.findById(clientId)).thenReturn(Optional.empty());

        GetSessionDetailsUseCase useCase = new GetSessionDetailsUseCase(securityUtils, sessionRepository, userRepository, whatsAppLinkGenerator);

        assertThrows(UserNotFoundException.class, () -> useCase.execute(sessionId));
    }
}

