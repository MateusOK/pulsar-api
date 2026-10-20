package com.soupulsar.application.partner;

import com.soupulsar.application.interfaces.TokenHasher;
import com.soupulsar.application.utils.SecurityUtils;
import com.soupulsar.domain.model.partner.Partner;
import com.soupulsar.domain.model.partner.PartnerDiscount;
import com.soupulsar.domain.model.partner.PartnerDiscountCode;
import com.soupulsar.domain.model.partner.PartnerDiscountCodeStatus;
import com.soupulsar.domain.model.vo.Percentage;
import com.soupulsar.domain.repository.ClientPartnerBenefitRepository;
import com.soupulsar.domain.repository.PartnerDiscountCodeRepository;
import com.soupulsar.domain.repository.PartnerDiscountRepository;
import com.soupulsar.domain.repository.PartnerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;

class RedeemPartnerDiscountCodeUseCaseTest {

    private SecurityUtils securityUtils;
    private PartnerRepository partnerRepository;
    private PartnerDiscountRepository discountRepository;
    private PartnerDiscountCodeRepository codeRepository;
    private ClientPartnerBenefitRepository benefitRepository;
    private TokenHasher tokenHasher;
    private Clock clock;
    private RedeemPartnerDiscountCodeUseCase useCase;

    @BeforeEach
    void setUp() {
        securityUtils = mock(SecurityUtils.class);
        partnerRepository = mock(PartnerRepository.class);
        discountRepository = mock(PartnerDiscountRepository.class);
        codeRepository = mock(PartnerDiscountCodeRepository.class);
        benefitRepository = mock(ClientPartnerBenefitRepository.class);
        tokenHasher = mock(TokenHasher.class);
        clock = Clock.fixed(Instant.now(), ZoneOffset.UTC);
        useCase = new RedeemPartnerDiscountCodeUseCase(
                securityUtils, partnerRepository, discountRepository, codeRepository,
                benefitRepository, tokenHasher, clock);
    }

    @Test
    void redeemsValidCodeAndActivatesClientBenefit() {
        Instant now = clock.instant();
        UUID clientId = UUID.randomUUID();
        UUID partnerId = UUID.randomUUID();
        UUID discountId = UUID.randomUUID();
        PartnerDiscountCode code = PartnerDiscountCode.restore(
                UUID.randomUUID(), discountId, clientId, "user@example.com", "hashed-code",
                PartnerDiscountCodeStatus.ACTIVE, now.plus(Duration.ofMinutes(15)), null);
        PartnerDiscount discount = PartnerDiscount.restore(
                discountId, partnerId, new Percentage(new BigDecimal("10")), true, now.plus(Duration.ofDays(1)));

        when(securityUtils.getCurrentUserId()).thenReturn(clientId);
        when(tokenHasher.hash("PARTNERCODE")).thenReturn("hashed-code");
        when(codeRepository.findByClientIdAndCodeHash(clientId, "hashed-code")).thenReturn(Optional.of(code));
        when(discountRepository.findById(discountId)).thenReturn(Optional.of(discount));
        when(partnerRepository.findById(partnerId)).thenReturn(Optional.of(Partner.restore(partnerId, "Partner", true)));

        useCase.execute(new RedeemPartnerDiscountCodeRequest(" partner code "));

        assertEquals(PartnerDiscountCodeStatus.REDEEMED, code.getStatus());
        assertEquals(now, code.getRedeemedAt());
        verify(benefitRepository).save(argThat(benefit ->
                benefit.getClientId().equals(clientId)
                        && benefit.getPartnerId().equals(partnerId)
                        && benefit.isActive()
                        && benefit.getActivatedAt().equals(now)));
        verify(codeRepository).save(code);
    }

    @Test
    void rejectsCodeThatCannotBeFoundAndDoesNotCreateBenefit() {
        UUID clientId = UUID.randomUUID();
        when(securityUtils.getCurrentUserId()).thenReturn(clientId);
        when(tokenHasher.hash("MISSING")).thenReturn("hashed-missing");
        when(codeRepository.findByClientIdAndCodeHash(clientId, "hashed-missing")).thenReturn(Optional.empty());

        assertEquals("Invalid discount code",
                assertThrows(IllegalArgumentException.class,
                        () -> useCase.execute(new RedeemPartnerDiscountCodeRequest("missing"))).getMessage());

        verifyNoInteractions(discountRepository, partnerRepository, benefitRepository);
        verify(codeRepository, never()).save(any());
    }

    @Test
    void rejectsRedeemedCode() {
        Instant now = clock.instant();
        UUID clientId = UUID.randomUUID();
        UUID discountId = UUID.randomUUID();
        PartnerDiscountCode code = PartnerDiscountCode.restore(
                UUID.randomUUID(), discountId, clientId, "user@example.com", "hashed-code",
                PartnerDiscountCodeStatus.REDEEMED, now.plus(Duration.ofMinutes(15)), now.minusSeconds(1));

        when(securityUtils.getCurrentUserId()).thenReturn(clientId);
        when(tokenHasher.hash("CODE")).thenReturn("hashed-code");
        when(codeRepository.findByClientIdAndCodeHash(clientId, "hashed-code")).thenReturn(Optional.of(code));

        assertEquals("Discount code is not redeemable",
                assertThrows(IllegalArgumentException.class,
                        () -> useCase.execute(new RedeemPartnerDiscountCodeRequest("code"))).getMessage());

        verifyNoInteractions(discountRepository, partnerRepository, benefitRepository);
        verify(codeRepository, never()).save(any());
    }
}
