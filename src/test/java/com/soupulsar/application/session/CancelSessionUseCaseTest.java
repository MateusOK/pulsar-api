package com.soupulsar.application.session;

import com.soupulsar.application.dto.response.SessionResponse;
import com.soupulsar.application.interfaces.PaymentGateway;
import com.soupulsar.application.session.CancelSessionUseCase;
import com.soupulsar.application.utils.SecurityUtils;
import com.soupulsar.domain.exceptions.SessionNotFoundException;
import com.soupulsar.domain.model.enums.PaymentMethod;
import com.soupulsar.domain.model.enums.PaymentStatus;
import com.soupulsar.domain.model.enums.SessionStatus;
import com.soupulsar.domain.model.payment.Payment;
import com.soupulsar.domain.model.session.Session;
import com.soupulsar.domain.model.vo.Money;
import com.soupulsar.domain.model.vo.PaymentAmounts;
import com.soupulsar.domain.model.vo.PaymentSplit;
import com.soupulsar.domain.repository.PaymentRepository;
import com.soupulsar.domain.repository.SessionRepository;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Clock;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class CancelSessionUseCaseTest {

    @Test
    void execute_shouldCancelSessionAndRefundWhenSpecialistCancels() {
        SessionRepository sessionRepository = mock(SessionRepository.class);
        PaymentRepository paymentRepository = mock(PaymentRepository.class);
        PaymentGateway paymentGateway = mock(PaymentGateway.class);
        SecurityUtils securityUtils = mock(SecurityUtils.class);

        UUID specialistId = UUID.randomUUID();
        UUID clientId = UUID.randomUUID();
        UUID sessionId = UUID.randomUUID();
        LocalDateTime now = LocalDateTime.of(2026, 7, 22, 10, 0);
        Clock clock = Clock.fixed(now.atZone(ZoneId.systemDefault()).toInstant(), ZoneId.systemDefault());

        Session session = Session.restore(sessionId, specialistId, clientId, now.plusDays(3), now.plusDays(4), SessionStatus.CONFIRMED);
        Payment payment = Payment.create(sessionId, specialistId, clientId,
                new PaymentAmounts(new Money(new BigDecimal("100.00")), Money.zero()),
                new PaymentSplit(new Money(new BigDecimal("100.00")), new Money(new BigDecimal("0.00"))),
                PaymentMethod.PIX);
        payment.markAsPending("ext-123", "https://pay.example.com");
        payment.markAsPaid();

        when(securityUtils.getCurrentUserId()).thenReturn(specialistId);
        when(sessionRepository.findBySessionId(sessionId)).thenReturn(Optional.of(session));
        when(paymentRepository.findBySessionId(sessionId)).thenReturn(Optional.of(payment));

        CancelSessionUseCase useCase = new CancelSessionUseCase(sessionRepository, paymentRepository, paymentGateway, securityUtils, clock);
        SessionResponse response = useCase.execute(sessionId);

        assertNotNull(response);
        assertEquals(SessionStatus.CANCELLED, response.status());
        assertEquals(PaymentStatus.REFUND_PENDING, payment.getPaymentStatus());
        verify(paymentRepository).findBySessionId(sessionId);
        verify(paymentRepository).saveAndFlush(payment);
        verify(sessionRepository).saveAndFlush(session);
        verify(paymentGateway).refundPayment("ext-123", 100.00d);
    }

    @Test
    void execute_shouldCancelSessionWithoutRefundWhenClientCancelsWithin24Hours() {
        SessionRepository sessionRepository = mock(SessionRepository.class);
        PaymentRepository paymentRepository = mock(PaymentRepository.class);
        PaymentGateway paymentGateway = mock(PaymentGateway.class);
        SecurityUtils securityUtils = mock(SecurityUtils.class);

        UUID specialistId = UUID.randomUUID();
        UUID clientId = UUID.randomUUID();
        UUID sessionId = UUID.randomUUID();
        LocalDateTime now = LocalDateTime.of(2026, 7, 22, 10, 0);
        Clock clock = Clock.fixed(now.atZone(ZoneId.systemDefault()).toInstant(), ZoneId.systemDefault());

        Session session = Session.restore(sessionId, specialistId, clientId, now.plusHours(12), now.plusHours(13), SessionStatus.CONFIRMED);

        when(securityUtils.getCurrentUserId()).thenReturn(clientId);
        when(sessionRepository.findBySessionId(sessionId)).thenReturn(Optional.of(session));

        CancelSessionUseCase useCase = new CancelSessionUseCase(sessionRepository, paymentRepository, paymentGateway, securityUtils, clock);
        SessionResponse response = useCase.execute(sessionId);

        assertNotNull(response);
        assertEquals(SessionStatus.CANCELLED, response.status());
        verify(paymentRepository, never()).findBySessionId(any());
        verify(paymentGateway, never()).refundPayment(anyString(), anyDouble());
        verify(sessionRepository).saveAndFlush(session);
    }

    @Test
    void execute_shouldThrowWhenSessionNotFound() {
        SessionRepository sessionRepository = mock(SessionRepository.class);
        PaymentRepository paymentRepository = mock(PaymentRepository.class);
        PaymentGateway paymentGateway = mock(PaymentGateway.class);
        SecurityUtils securityUtils = mock(SecurityUtils.class);
        Clock clock = Clock.systemDefaultZone();

        UUID sessionId = UUID.randomUUID();
        when(sessionRepository.findBySessionId(sessionId)).thenReturn(Optional.empty());

        CancelSessionUseCase useCase = new CancelSessionUseCase(sessionRepository, paymentRepository, paymentGateway, securityUtils, clock);

        assertThrows(SessionNotFoundException.class, () -> useCase.execute(sessionId));
    }

    @Test
    void execute_shouldThrowWhenUserIsNotAuthorizedToCancel() {
        SessionRepository sessionRepository = mock(SessionRepository.class);
        PaymentRepository paymentRepository = mock(PaymentRepository.class);
        PaymentGateway paymentGateway = mock(PaymentGateway.class);
        SecurityUtils securityUtils = mock(SecurityUtils.class);

        UUID specialistId = UUID.randomUUID();
        UUID clientId = UUID.randomUUID();
        UUID anotherUserId = UUID.randomUUID();
        UUID sessionId = UUID.randomUUID();
        LocalDateTime now = LocalDateTime.of(2026, 7, 22, 10, 0);
        Clock clock = Clock.fixed(now.atZone(ZoneId.systemDefault()).toInstant(), ZoneId.systemDefault());

        Session session = Session.restore(sessionId, specialistId, clientId, now.plusDays(2), now.plusDays(3), SessionStatus.CONFIRMED);

        when(securityUtils.getCurrentUserId()).thenReturn(anotherUserId);
        when(sessionRepository.findBySessionId(sessionId)).thenReturn(Optional.of(session));

        CancelSessionUseCase useCase = new CancelSessionUseCase(sessionRepository, paymentRepository, paymentGateway, securityUtils, clock);

        assertThrows(IllegalArgumentException.class, () -> useCase.execute(sessionId));
        verify(sessionRepository, never()).saveAndFlush(any());
    }
}
