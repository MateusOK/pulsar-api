package com.soupulsar.infrastructure.persistence.repository;

import com.soupulsar.domain.model.enums.SpecialistType;
import com.soupulsar.infrastructure.persistence.entity.payment.PaymentSplitRuleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface PaymentSplitRuleJpaRepository extends JpaRepository<PaymentSplitRuleEntity, UUID> {

    @Query("""
           SELECT p FROM PaymentSplitRuleEntity p
           WHERE p.active = true
           AND (p.specialistId = :specialistId OR p.specialistId IS NULL)
           AND (p.specialistType = :specialistType OR p.specialistType IS NULL)
           """)
    List<PaymentSplitRuleEntity> findActiveApplicableRules(UUID specialistId, SpecialistType specialistType);

}