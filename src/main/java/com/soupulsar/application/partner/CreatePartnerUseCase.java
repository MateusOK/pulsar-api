package com.soupulsar.application.partner;

import com.soupulsar.domain.model.partner.Partner;
import com.soupulsar.domain.repository.PartnerRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CreatePartnerUseCase {

    private final PartnerRepository partnerRepository;

    public void execute(CreatePartnerRequest request) {

        Partner partner = Partner.create(request.name());
        partnerRepository.save(partner);

    }
}