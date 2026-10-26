package com.soupulsar.domain.repository;

import com.soupulsar.domain.model.payment.Payment;

import java.util.Optional;
import java.util.UUID;

public interface PaymentRepository {

    Optional<Payment> findById(UUID id);
    Payment save(Payment payment);
    Payment saveAndFlush(Payment payment);
    Optional<Payment> findByExternalPaymentId(String externalPaymentId);
    Optional<Payment> findBySessionId(UUID sessionId);

}
