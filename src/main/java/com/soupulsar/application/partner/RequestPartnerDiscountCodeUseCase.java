package com.soupulsar.application.partner;

import com.soupulsar.application.interfaces.TokenGenerator;
import com.soupulsar.application.interfaces.TokenHasher;
import com.soupulsar.application.utils.SecurityUtils;
import com.soupulsar.domain.model.partner.Partner;
import com.soupulsar.domain.model.partner.PartnerDiscount;
import com.soupulsar.domain.model.partner.PartnerDiscountCode;
import com.soupulsar.domain.model.partner.PartnerEligibility;
import com.soupulsar.domain.repository.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class RequestPartnerDiscountCodeUseCase {

    private final SecurityUtils securityUtils;
    private final PartnerRepository partnerRepository;
    private final PartnerEligibilityRepository partnerEligibilityRepository;
    private final PartnerDiscountRepository partnerDiscountRepository;
    private final PartnerDiscountCodeRepository partnerDiscountCodeRepository;
    private final ClientPartnerBenefitRepository clientPartnerBenefitRepository;
    private final TokenGenerator tokenGenerator;
    private final TokenHasher tokenHasher;
    private final ApplicationEventPublisher eventPublisher;
    private final Clock clock;

    @Transactional
    public void execute(PartnerDiscountCodeRequest request){

        UUID clientId = securityUtils.getCurrentUserId();
        Instant now = clock.instant();

        String normalizedEmail = normalizeEmail(request.email());
        String domain = extractDomainFromEmail(normalizedEmail);

            List<PartnerEligibility> eligibilities = partnerEligibilityRepository.findMatchingEligibility(normalizedEmail, domain);

        if (eligibilities.isEmpty()) {
            return;
        }

        Set<UUID> discountIds = eligibilities.stream()
                .map(PartnerEligibility::getPartnerDiscountId)
                .collect(Collectors.toSet());

        if (discountIds.isEmpty()) {
            return;
        }

        List<PartnerDiscount> applicableDiscounts = partnerDiscountRepository.findAllByIds(discountIds)
                .stream()
                .filter(discount -> discount.isApplicableAt(now))
                .toList();

        if (applicableDiscounts.isEmpty()) {
            return;
        }

        Set<UUID> partnerIds = applicableDiscounts.stream()
                .map(PartnerDiscount::getPartnerId)
                .collect(Collectors.toSet());

        Set<UUID> activePartnerIds = partnerRepository.findAllByIds(partnerIds)
                .stream()
                .filter(Partner::isActive)
                .map(Partner::getId)
                .collect(Collectors.toSet());

        if (activePartnerIds.isEmpty()) {
            return;
        }

        Set<UUID> existingPartnerIds = clientPartnerBenefitRepository.findPartnerIdsWithExistingBenefit(clientId, activePartnerIds);

        List<PartnerDiscount> eligibleDiscounts = applicableDiscounts.stream()
                .filter(discount -> activePartnerIds.contains(discount.getPartnerId()))
                .filter(discount -> !existingPartnerIds.contains(discount.getPartnerId()))
                .toList();

        if (eligibleDiscounts.isEmpty()) {
            return;
        }



        eligibleDiscounts.forEach(discount -> issueCode(clientId, normalizedEmail, discount, now));

    }

    private void issueCode(UUID clientId, String email, PartnerDiscount discount, Instant now) {

        partnerDiscountCodeRepository.invalidateActiveCodes(discount.getId(), clientId);

        String rawToken = tokenGenerator.generatePartnerCode();
        String hashedToken = tokenHasher.hash(rawToken);

        Instant expiresAt = now.plus(Duration.ofMinutes(15));

        PartnerDiscountCode code = PartnerDiscountCode.create(discount.getId(), clientId, email, hashedToken, expiresAt);

        partnerDiscountCodeRepository.save(code);

        eventPublisher.publishEvent(new PartnerDiscountCodeIssuedEvent(rawToken, email, expiresAt));

    }

    private String normalizeEmail(String email) {
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("Email cannot be null or blank");
        }

        String normalized = email.trim().toLowerCase(Locale.ROOT);

        int atIndex = normalized.indexOf('@');
        if (atIndex <= 0
                || atIndex != normalized.lastIndexOf('@')
                || atIndex == normalized.length() - 1) {
            throw new IllegalArgumentException("Invalid email format");
        }

        String localPart = normalized.substring(0, atIndex);

        if (localPart.contains("+")){
            throw new IllegalArgumentException("Email aliases are not allowed for partner verification");
        }
        return normalized;
    }

    private String extractDomainFromEmail(String email) {
        int separatorIndex = email.indexOf('@');

        if (separatorIndex <= 0 || separatorIndex == email.length() - 1) {
            throw new IllegalArgumentException("Invalid email format");
        }
        return email.substring(separatorIndex + 1);
    }
}