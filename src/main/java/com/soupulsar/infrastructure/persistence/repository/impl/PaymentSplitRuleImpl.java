package com.soupulsar.infrastructure.persistence.repository.impl;

import com.soupulsar.domain.model.enums.SpecialistType;
import com.soupulsar.domain.model.payment.PaymentSplitRule;
import com.soupulsar.domain.repository.PaymentSplitRuleRepository;
import com.soupulsar.infrastructure.persistence.entity.payment.PaymentSplitRuleEntity;
import com.soupulsar.infrastructure.persistence.mapper.payment.PaymentSplitRuleMapper;
import com.soupulsar.infrastructure.persistence.repository.PaymentSplitRuleJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
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

    @Override
    public List<PaymentSplitRule> findActiveApplicableRules(UUID specialistId, SpecialistType specialistType) {
        var rules = repository.findActiveApplicableRules(specialistId, specialistType);
        if (rules != null && !rules.isEmpty()) {
            return rules.stream()
                    .map(PaymentSplitRuleMapper::toModel)
                    .toList();
        }
        return List.of();
    }
}