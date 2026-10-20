package com.soupulsar.domain.repository;

import com.soupulsar.domain.model.partner.PartnerEligibility;

import java.util.List;

public interface PartnerEligibilityRepository {

    List<PartnerEligibility> findMatchingEligibility(String email, String domain);

    PartnerEligibility save(PartnerEligibility partnerEligibility);

}
