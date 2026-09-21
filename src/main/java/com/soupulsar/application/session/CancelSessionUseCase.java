package com.soupulsar.application.session;

import com.soupulsar.application.dto.response.SessionResponse;
import com.soupulsar.application.interfaces.PaymentGateway;
import com.soupulsar.application.utils.SecurityUtils;
import com.soupulsar.domain.exceptions.SessionNotFoundException;
import com.soupulsar.domain.model.payment.Payment;
import com.soupulsar.domain.model.session.Session;
import com.soupulsar.domain.repository.PaymentRepository;
import com.soupulsar.domain.repository.SessionRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
public class CancelSessionUseCase {

    private final SessionRepository sessionRepository;
    private final PaymentRepository paymentRepository;
    private final PaymentGateway paymentGateway;
    private final SecurityUtils securityUtils;
    private final Clock clock;

    @Transactional
    public SessionResponse execute(UUID sessionId) {

        UUID requesterId = securityUtils.getCurrentUserId();
        LocalDateTime now = LocalDateTime.now(clock);

        Session session = sessionRepository.findBySessionId(sessionId)
                .orElseThrow(() -> new SessionNotFoundException(sessionId));

        boolean isSpecialist = session.getSpecialistId().equals(requesterId);
        boolean isClient = session.getClientId().equals(requesterId);

        if (!isSpecialist && !isClient) {
            throw new IllegalArgumentException("You are not authorized to cancel this session");
        }

        boolean shouldRefund = shouldRefund(session, requesterId, now);

        Payment payment = null;

        if (shouldRefund) {
            payment = paymentRepository.findBySessionId(sessionId)
                    .orElseThrow(() -> new IllegalArgumentException("Payment not found for this session"));
            payment.requestRefund();
        }

        session.cancelSession();
        sessionRepository.saveAndFlush(session);

        if (payment != null) {
            paymentRepository.saveAndFlush(payment);
            log.info("Refunding payment {} for session {}. Amount: {}", payment.getId(), sessionId, payment.getAmounts().getFinalAmount().toDouble());
            paymentGateway.refundPayment(payment.getExternalPaymentId(), payment.getAmounts().getFinalAmount().toDouble());
        }
        return new SessionResponse(session);
    }

    private boolean shouldRefund(Session session, UUID requesterId, LocalDateTime now) {
        if (session.getSpecialistId().equals(requesterId)) {
            return true;
        }
        return now.isBefore(session.getStartAt().minusHours(24));
    }
}