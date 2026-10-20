package com.soupulsar.domain.repository;

import com.soupulsar.domain.model.partner.ClientPartnerBenefit;

import java.util.Set;
import java.util.UUID;

public interface ClientPartnerBenefitRepository {

    Set<UUID> findPartnerIdsWithExistingBenefit(UUID clientId, Set<UUID> partnerIds);

    ClientPartnerBenefit save(ClientPartnerBenefit benefit);

}