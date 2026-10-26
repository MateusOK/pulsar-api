package com.soupulsar.infrastructure.persistence.repository;

import com.soupulsar.infrastructure.persistence.entity.partner.PartnerDiscountEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PartnerDiscountJpaRepository extends JpaRepository<PartnerDiscountEntity, UUID> {
}