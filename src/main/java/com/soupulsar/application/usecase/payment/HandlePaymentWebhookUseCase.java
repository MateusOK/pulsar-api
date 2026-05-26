package com.soupulsar.application.usecase.payment;

import com.soupulsar.application.usecase.session.ConfirmSessionUseCase;
import com.soupulsar.domain.model.enums.GatewayPaymentEvent;
import com.soupulsar.domain.model.enums.PaymentStatus;
import com.soupulsar.domain.model.payment.Payment;
import com.soupulsar.domain.model.payment.WebhookEvent;
import com.soupulsar.domain.repository.PaymentRepository;
import com.soupulsar.domain.repository.WebhookEventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;

@RequiredArgsConstructor
public class HandlePaymentWebhookUseCase {

    private final WebhookEventRepository webhookEventRepository;
    private final PaymentRepository paymentRepository;
    private final ConfirmSessionUseCase confirmSessionUseCase;

    public void execute(String externalEventId, String externalPaymentId, GatewayPaymentEvent event){

        if (externalEventId == null || externalEventId.isEmpty()){
            throw new IllegalArgumentException("External event ID cannot be null or empty");
        }
        if (webhookEventRepository.existsByExternalEventId(externalEventId)){
            return;
        }

        try {
            webhookEventRepository.save(WebhookEvent.create(externalEventId, externalPaymentId, event));
        } catch (DataIntegrityViolationException e) {
            return;
        }

        Payment payment = paymentRepository.findByExternalPaymentId(externalPaymentId)
                .orElseThrow(() -> new IllegalArgumentException("Payment not found for external payment ID: " + externalPaymentId));

        boolean changed = payment.handleGatewayEvent(event);

        if (!changed) {
            return;
        }

        paymentRepository.save(payment);

        if (payment.getPaymentStatus() == PaymentStatus.PAID) {
            confirmSessionUseCase.execute(payment.getSessionId());
        }
    }
}