package com.soupulsar.infrastructure.persistence.repository;

import com.soupulsar.domain.model.partner.PartnerDiscountCodeStatus;
import com.soupulsar.infrastructure.persistence.entity.partner.PartnerDiscountCodeEntity;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface PartnerDiscountCodeJpaRepository extends JpaRepository<PartnerDiscountCodeEntity, UUID> {

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("""
            UPDATE PartnerDiscountCodeEntity c
            SET c.status = :invalidatedStatus
            WHERE c.partnerDiscountId = :partnerDiscountId
            AND c.clientId = :clientId
            AND c.status = :activeStatus
            """)
    void invalidateActiveCodes(@Param("partnerDiscountId") UUID partnerDiscountId, @Param("clientId") UUID clientId,
                               @Param("activeStatus") PartnerDiscountCodeStatus activeStatus, @Param("invalidatedStatus") PartnerDiscountCodeStatus invalidatedStatus);


    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("""
            SELECT c
            FROM PartnerDiscountCodeEntity c
            WHERE c.clientId = :clientId
            AND c.codeHash = :codeHash
            """)
    Optional<PartnerDiscountCodeEntity> findByClientIdAndCodeHash(@Param("clientId") UUID clientId, @Param("codeHash") String codeHash);
}