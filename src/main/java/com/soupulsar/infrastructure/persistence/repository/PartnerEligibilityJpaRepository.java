package com.soupulsar.infrastructure.persistence.repository;

import com.soupulsar.domain.model.partner.PartnerEligibilityType;
import com.soupulsar.infrastructure.persistence.entity.partner.PartnerEligibilityEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface PartnerEligibilityJpaRepository extends JpaRepository<PartnerEligibilityEntity, UUID> {

    @Query("""
            SELECT pe FROM PartnerEligibilityEntity pe
            WHERE (pe.type = :emailType AND pe.value = :email)
               OR (pe.type = :domainType AND pe.value = :domain)
            """)
    List<PartnerEligibilityEntity> findMatchingEligibility(@Param("emailType")PartnerEligibilityType emailType, @Param("domainType")PartnerEligibilityType domainType, @Param("email") String email, @Param("domain") String domain);

}