package com.soupulsar.application.partner;

import com.soupulsar.domain.model.partner.PartnerEligibility;
import com.soupulsar.domain.repository.PartnerEligibilityRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CreatePartnerEligibilityUseCase {

    private final PartnerEligibilityRepository partnerEligibilityRepository;

    public void execute(CreatePartnerEligibilityRequest request) {

        PartnerEligibility partnerEligibility = PartnerEligibility.create(request.partnerId(), request.type(), request.value());

        partnerEligibilityRepository.save(partnerEligibility);
    }
}