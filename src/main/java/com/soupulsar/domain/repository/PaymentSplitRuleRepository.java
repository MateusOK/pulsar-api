package com.soupulsar.domain.repository;

import com.soupulsar.domain.model.enums.SpecialistType;
import com.soupulsar.domain.model.payment.PaymentSplitRule;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PaymentSplitRuleRepository {

    Optional<PaymentSplitRule> findById(UUID id);
    PaymentSplitRule save(PaymentSplitRule paymentSplitRule);
    List<PaymentSplitRule> findActiveApplicableRules(UUID specialistId, SpecialistType specialistType);

}
