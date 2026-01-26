package com.soupulsar.infrastructure.persistence.repository;

import com.soupulsar.infrastructure.persistence.entity.payment.PaymentSplitRuleEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PaymentSplitRuleJpaRepository extends JpaRepository<PaymentSplitRuleEntity, UUID> {
}