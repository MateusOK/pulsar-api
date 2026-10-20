package com.soupulsar.application.partner;

import com.soupulsar.application.interfaces.TokenHasher;
import com.soupulsar.application.utils.SecurityUtils;
import com.soupulsar.domain.model.partner.ClientPartnerBenefit;
import com.soupulsar.domain.model.partner.Partner;
import com.soupulsar.domain.model.partner.PartnerDiscount;
import com.soupulsar.domain.model.partner.PartnerDiscountCode;
import com.soupulsar.domain.repository.ClientPartnerBenefitRepository;
import com.soupulsar.domain.repository.PartnerDiscountCodeRepository;
import com.soupulsar.domain.repository.PartnerDiscountRepository;
import com.soupulsar.domain.repository.PartnerRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import java.time.Clock;
import java.time.Instant;
import java.util.Locale;
import java.util.UUID;

@RequiredArgsConstructor
public class RedeemPartnerDiscountCodeUseCase {

    private final SecurityUtils securityUtils;
    private final PartnerRepository partnerRepository;
    private final PartnerDiscountRepository partnerDiscountRepository;
    private final PartnerDiscountCodeRepository partnerDiscountCodeRepository;
    private final ClientPartnerBenefitRepository clientPartnerBenefitRepository;
    private final TokenHasher tokenHasher;
    private final Clock clock;

    @Transactional
    public void execute(RedeemPartnerDiscountCodeRequest request) {

        UUID clientId = securityUtils.getCurrentUserId();
        Instant now = clock.instant();

        String normalizedCode = normalizeCode(request.code());
        String codeHash = tokenHasher.hash(normalizedCode);

        PartnerDiscountCode partnerDiscountCode = partnerDiscountCodeRepository.findByClientIdAndCodeHash(clientId, codeHash)
                .orElseThrow(() -> new IllegalArgumentException("Invalid discount code"));

        if (!partnerDiscountCode.isRedeemableAt(now)) {
            throw new IllegalArgumentException("Discount code is not redeemable");
        }

        PartnerDiscount discount = partnerDiscountRepository.findById(partnerDiscountCode.getPartnerDiscountId())
                .orElseThrow(() -> new IllegalArgumentException("Associated discount not found"));

        if (!discount.isApplicableAt(now)) {
            throw new IllegalArgumentException("Associated discount is not applicable at this time");
        }

        Partner partner = partnerRepository.findById(discount.getPartnerId())
                .orElseThrow(() -> new IllegalArgumentException("Associated partner not found"));

        if (!partner.isActive()) {
            throw new IllegalArgumentException("Associated partner is not active");
        }

        ClientPartnerBenefit benefit = ClientPartnerBenefit.create(clientId, partner.getId(), now);

        clientPartnerBenefitRepository.save(benefit);

        partnerDiscountCode.redeem(now);
        partnerDiscountCodeRepository.save(partnerDiscountCode);

    }

    private String normalizeCode(String code) {
        if (code == null || code.isBlank()) {
            throw new IllegalArgumentException("Code cannot be null or blank");
        }
        return code.trim().replace(" ", "").toUpperCase(Locale.ROOT);
    }
}