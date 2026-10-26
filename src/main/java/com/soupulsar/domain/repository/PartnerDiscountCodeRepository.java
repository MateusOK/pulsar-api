package com.soupulsar.domain.repository;

import com.soupulsar.domain.model.partner.PartnerDiscountCode;

import java.util.Optional;
import java.util.UUID;

public interface PartnerDiscountCodeRepository {

    PartnerDiscountCode save(PartnerDiscountCode partnerDiscountCode);

    void invalidateActiveCodes(UUID partnerDiscountId, UUID clientId);

    Optional<PartnerDiscountCode> findByClientIdAndCodeHash(UUID clientId, String codeHash);

}