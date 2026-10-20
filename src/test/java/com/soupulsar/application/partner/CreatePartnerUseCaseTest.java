package com.soupulsar.application.partner;

import com.soupulsar.domain.model.partner.Partner;
import com.soupulsar.domain.repository.PartnerRepository;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class CreatePartnerUseCaseTest {

    @Test
    void createsAndPersistsActivePartner() {
        PartnerRepository repository = mock(PartnerRepository.class);
        CreatePartnerUseCase useCase = new CreatePartnerUseCase(repository);

        useCase.execute(new CreatePartnerRequest("SouPulsar Partner"));

        verify(repository).save(argThat(partner -> {
            assertNotNull(partner.getId());
            assertEquals("SouPulsar Partner", partner.getName());
            return partner.isActive();
        }));
    }
}
