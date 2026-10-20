package com.soupulsar.domain.repository;

import com.soupulsar.domain.model.partner.PartnerDiscount;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public interface PartnerDiscountRepository {

    List<PartnerDiscount> findAllByIds(Set<UUID> ids);

    Optional<PartnerDiscount> findById(UUID id);

    PartnerDiscount save(PartnerDiscount partnerDiscount);

}
