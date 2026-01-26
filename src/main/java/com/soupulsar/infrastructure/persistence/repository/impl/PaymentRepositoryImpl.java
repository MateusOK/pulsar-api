package com.soupulsar.infrastructure.persistence.repository.impl;

import com.soupulsar.domain.model.payment.Payment;
import com.soupulsar.domain.repository.PaymentRepository;
import com.soupulsar.infrastructure.persistence.entity.payment.PaymentEntity;
import com.soupulsar.infrastructure.persistence.mapper.payment.PaymentMapper;
import com.soupulsar.infrastructure.persistence.repository.PaymentJpaRepository;
import lombok.RequiredArgsConstructor;

import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
public class PaymentRepositoryImpl implements PaymentRepository {

    private final PaymentJpaRepository paymentJpaRepository;

    @Override
    public Optional<Payment> findById(UUID id) {
        return paymentJpaRepository.findById(id).map(PaymentMapper::toModel);
    }

    @Override
    public Payment save(Payment payment) {
        PaymentEntity entity = PaymentMapper.toEntity(payment);
        PaymentEntity saved = paymentJpaRepository.save(entity);
        return PaymentMapper.toModel(saved);
    }
}