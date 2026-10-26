package com.soupulsar.application.partner;

import com.soupulsar.domain.model.partner.PartnerDiscount;
import com.soupulsar.domain.model.vo.Percentage;
import com.soupulsar.domain.repository.PartnerDiscountRepository;
import lombok.RequiredArgsConstructor;

import java.time.Clock;
import java.time.Instant;

@RequiredArgsConstructor
public class CreatePartnerDiscountUseCase {

    private final PartnerDiscountRepository partnerDiscountRepository;
    private final Clock clock;

    public void execute(CreatePartnerDiscountRequest request) {

        Instant now = clock.instant();

        PartnerDiscount partnerDiscount = PartnerDiscount.create(request.partnerId(), new Percentage(request.discountPercentage()), request.expiresAt(), now);
        partnerDiscountRepository.save(partnerDiscount);

    }
}