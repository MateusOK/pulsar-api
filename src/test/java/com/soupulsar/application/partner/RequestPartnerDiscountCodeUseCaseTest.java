package com.soupulsar.application.partner;

import com.soupulsar.application.interfaces.TokenGenerator;
import com.soupulsar.application.interfaces.TokenHasher;
import com.soupulsar.application.utils.SecurityUtils;
import com.soupulsar.domain.model.partner.Partner;
import com.soupulsar.domain.model.partner.PartnerDiscount;
import com.soupulsar.domain.model.partner.PartnerEligibility;
import com.soupulsar.domain.model.partner.PartnerEligibilityType;
import com.soupulsar.domain.model.vo.Percentage;
import com.soupulsar.domain.repository.ClientPartnerBenefitRepository;
import com.soupulsar.domain.repository.PartnerDiscountCodeRepository;
import com.soupulsar.domain.repository.PartnerDiscountRepository;
import com.soupulsar.domain.repository.PartnerEligibilityRepository;
import com.soupulsar.domain.repository.PartnerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationEventPublisher;

import java.math.BigDecimal;
import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class RequestPartnerDiscountCodeUseCaseTest {

    private SecurityUtils securityUtils;
    private PartnerRepository partnerRepository;
    private PartnerEligibilityRepository eligibilityRepository;
    private PartnerDiscountRepository discountRepository;
    private PartnerDiscountCodeRepository codeRepository;
    private ClientPartnerBenefitRepository benefitRepository;
    private TokenGenerator tokenGenerator;
    private TokenHasher tokenHasher;
    private ApplicationEventPublisher eventPublisher;
    private Clock clock;
    private RequestPartnerDiscountCodeUseCase useCase;

    @BeforeEach
    void setUp() {
        securityUtils = mock(SecurityUtils.class);
        partnerRepository = mock(PartnerRepository.class);
        eligibilityRepository = mock(PartnerEligibilityRepository.class);
        discountRepository = mock(PartnerDiscountRepository.class);
        codeRepository = mock(PartnerDiscountCodeRepository.class);
        benefitRepository = mock(ClientPartnerBenefitRepository.class);
        tokenGenerator = mock(TokenGenerator.class);
        tokenHasher = mock(TokenHasher.class);
        eventPublisher = mock(ApplicationEventPublisher.class);
        clock = Clock.fixed(Instant.now(), Clock.systemUTC().getZone());
        useCase = new RequestPartnerDiscountCodeUseCase(
                securityUtils, partnerRepository, eligibilityRepository, discountRepository,
                codeRepository, benefitRepository, tokenGenerator, tokenHasher, eventPublisher, clock);
    }

    @Test
    void issuesHashedCodeForEligibleActivePartnerAndPublishesRawCodeEvent() {
        Instant now = clock.instant();
        UUID clientId = UUID.randomUUID();
        UUID partnerId = UUID.randomUUID();
        UUID discountId = UUID.randomUUID();
        String rawCode = "raw-code";
        String codeHash = "secure-hash";

        when(securityUtils.getCurrentUserId()).thenReturn(clientId);
        when(eligibilityRepository.findMatchingEligibility("user@example.com", "example.com"))
                .thenReturn(List.of(PartnerEligibility.restore(
                        UUID.randomUUID(), discountId, PartnerEligibilityType.DOMAIN, "example.com")));
        when(discountRepository.findAllByIds(Set.of(discountId)))
                .thenReturn(List.of(PartnerDiscount.restore(
                        discountId, partnerId, new Percentage(new BigDecimal("20")), true, now.plus(Duration.ofDays(1)))));
        when(partnerRepository.findAllByIds(Set.of(partnerId))).thenReturn(List.of(Partner.restore(partnerId, "Partner", true)));
        when(benefitRepository.findPartnerIdsWithExistingBenefit(clientId, Set.of(partnerId))).thenReturn(Set.of());
        when(tokenGenerator.generatePartnerCode()).thenReturn(rawCode);
        when(tokenHasher.hash(rawCode)).thenReturn(codeHash);

        useCase.execute(new PartnerDiscountCodeRequest("  User@Example.com "));

        verify(eligibilityRepository).findMatchingEligibility("user@example.com", "example.com");
        verify(codeRepository).invalidateActiveCodes(discountId, clientId);
        verify(codeRepository).save(argThat(code ->
                code.getPartnerDiscountId().equals(discountId)
                        && code.getClientId().equals(clientId)
                        && code.getRecipientEmail().equals("user@example.com")
                        && code.getCodeHash().equals(codeHash)
                        && code.getExpiresAt().equals(now.plus(Duration.ofMinutes(15)))));
        verify(eventPublisher).publishEvent(argThat((Object event) ->
                event instanceof PartnerDiscountCodeIssuedEvent issued
                        && issued.rawCode().equals(rawCode)
                        && issued.email().equals("user@example.com")
                        && issued.expiresAt().equals(now.plus(Duration.ofMinutes(15)))));
    }

    @Test
    void doesNotIssueCodeWhenNoEligibilityMatches() {
        when(securityUtils.getCurrentUserId()).thenReturn(UUID.randomUUID());
        when(eligibilityRepository.findMatchingEligibility("user@example.com", "example.com")).thenReturn(List.of());

        useCase.execute(new PartnerDiscountCodeRequest("user@example.com"));

        verifyNoInteractions(discountRepository, partnerRepository, benefitRepository, codeRepository, tokenGenerator, tokenHasher, eventPublisher);
    }

    @Test
    void rejectsEmailAliasesBeforeEligibilityLookup() {
        assertEquals("Email aliases are not allowed for partner verification",
                assertThrows(IllegalArgumentException.class,
                        () -> useCase.execute(new PartnerDiscountCodeRequest("user+tag@example.com"))).getMessage());

        verifyNoInteractions(eligibilityRepository, discountRepository, partnerRepository, benefitRepository, codeRepository, eventPublisher);
    }
}
