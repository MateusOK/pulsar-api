package com.soupulsar.application.usecase.payment;

import com.soupulsar.application.dto.request.AsaasWebhookRequest;
import com.soupulsar.application.session.ConfirmSessionUseCase;
import com.soupulsar.domain.model.enums.GatewayPaymentEvent;
import com.soupulsar.domain.model.enums.PaymentStatus;
import com.soupulsar.domain.model.payment.Payment;
import com.soupulsar.domain.model.payment.WebhookEvent;
import com.soupulsar.domain.repository.PaymentRepository;
import com.soupulsar.domain.repository.WebhookEventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.List;

@RequiredArgsConstructor
public class HandlePaymentWebhookUseCase {

    private final WebhookEventRepository webhookEventRepository;
    private final PaymentRepository paymentRepository;
    private final ConfirmSessionUseCase confirmSessionUseCase;

    public void execute(String externalEventId, String externalPaymentId, GatewayPaymentEvent event, List<AsaasWebhookRequest.Refund> refunds) {

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

        if (!handleGatewayEvent(payment, event, refunds)) {
            return;
        }

        paymentRepository.save(payment);

        if (payment.getPaymentStatus() == PaymentStatus.PAID) {
            confirmSessionUseCase.execute(payment.getSessionId());
        }

    }

    private boolean handleGatewayEvent(Payment payment, GatewayPaymentEvent event, List<AsaasWebhookRequest.Refund> refunds) {

        switch (event) {
            case PAID -> {
                payment.markAsPaid();
                return true;
            }
            case OVERDUE -> {
                payment.markAsOverdue();
                return true;
            }
            case FAILED -> {
                payment.markAsFailed();
                return true;
            }
            case REFUNDED -> {
                if (hasCompletedRefund(refunds)) {
                    payment.markAsRefunded();
                    return true;
                }
                return false;
            }
            default -> {
                return false;
            }
        }
    }

    private boolean hasCompletedRefund(List<AsaasWebhookRequest.Refund> refunds) {
        return refunds != null &&
                refunds.stream()
                        .anyMatch(refund -> "DONE".equalsIgnoreCase(refund.status()));
    }
}