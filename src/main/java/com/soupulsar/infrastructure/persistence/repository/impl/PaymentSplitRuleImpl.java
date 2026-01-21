package com.soupulsar.infrastructure.persistence.repository.impl;

import com.soupulsar.domain.model.payment.PaymentSplitRule;
import com.soupulsar.domain.repository.PaymentSplitRuleRepository;
import com.soupulsar.infrastructure.persistence.entity.payment.PaymentSplitRuleEntity;
import com.soupulsar.infrastructure.persistence.mapper.payment.PaymentSplitRuleMapper;
import com.soupulsar.infrastructure.persistence.repository.PaymentSplitRuleJpaRepository;
import lombok.RequiredArgsConstructor;

import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
public class PaymentSplitRuleImpl implements PaymentSplitRuleRepository {

    private final PaymentSplitRuleJpaRepository repository;

    @Override
    public Optional<PaymentSplitRule> findById(UUID id) {
        return repository.findById(id).map(PaymentSplitRuleMapper::toModel);
    }

    @Override
    public PaymentSplitRule save(PaymentSplitRule paymentSplitRule) {
        PaymentSplitRuleEntity entity = PaymentSplitRuleMapper.toEntity(paymentSplitRule);
        PaymentSplitRuleEntity saved = repository.save(entity);
        return PaymentSplitRuleMapper.toModel(saved);
    }
}