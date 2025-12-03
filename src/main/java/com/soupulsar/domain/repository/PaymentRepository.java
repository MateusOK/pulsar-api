package com.soupulsar.domain.repository;

import com.soupulsar.domain.model.Payment;

import java.util.Optional;
import java.util.UUID;

public interface PaymentRepository {

    Optional<Payment> findById(UUID id);
    Payment save(Payment payment);

}
