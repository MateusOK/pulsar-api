package com.soupulsar.infrastructure.persistence.repository;

import com.soupulsar.infrastructure.persistence.entity.partner.ClientPartnerBenefitEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Set;
import java.util.UUID;

public interface ClientPartnerBenefitJpaRepository extends JpaRepository<ClientPartnerBenefitEntity, UUID> {

    @Query("""
            SELECT benefit.partnerId
            FROM ClientPartnerBenefitEntity benefit
            WHERE benefit.clientId = :clientId
              AND benefit.partnerId IN :partnerIds
            """)
    Set<UUID> findPartnerIdsWithExistingBenefit(
            @Param("clientId") UUID clientId,
            @Param("partnerIds") Set<UUID> partnerIds
    );

}
