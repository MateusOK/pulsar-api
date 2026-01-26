package com.soupulsar.domain.repository;

import com.soupulsar.domain.model.payment.PaymentSplitRule;

import java.util.Optional;
import java.util.UUID;

public interface PaymentSplitRuleRepository {

    Optional<PaymentSplitRule> findById(UUID id);
    PaymentSplitRule save(PaymentSplitRule paymentSplitRule);

}
