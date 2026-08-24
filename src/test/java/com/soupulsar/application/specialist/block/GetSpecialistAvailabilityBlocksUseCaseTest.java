package com.soupulsar.application.specialist.block;

import com.soupulsar.application.utils.SecurityUtils;
import com.soupulsar.domain.model.availability.AvailabilityBlock;
import com.soupulsar.domain.repository.AvailabilityBlockRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GetSpecialistAvailabilityBlocksUseCaseTest {

    @Mock
    private AvailabilityBlockRepository repository;

    @Mock
    private SecurityUtils securityUtils;

    @InjectMocks
    private GetSpecialistAvailabilityBlocksUseCase useCase;

    @Test
    @DisplayName("Should return availability blocks for the current specialist")
    void shouldReturnAvailabilityBlocksForCurrentSpecialist() {
        UUID specialistId = UUID.randomUUID();

        LocalDateTime startsAt = LocalDateTime.of(2026, 8, 21, 9, 0);
        LocalDateTime endsAt = LocalDateTime.of(2026, 8, 21, 10, 0);

        AvailabilityBlock block =
                AvailabilityBlock.create(
                        specialistId,
                        startsAt,
                        endsAt,
                        "Personal appointment"
                );

        when(securityUtils.getCurrentUserId())
                .thenReturn(specialistId);

        when(repository.findAllBySpecialistId(specialistId))
                .thenReturn(List.of(block));

        GetSpecialistAvailabilityBlocksResponse response =
                useCase.execute();

        assertNotNull(response);
        assertEquals(1, response.blocks().size());

        verify(securityUtils).getCurrentUserId();
        verify(repository).findAllBySpecialistId(specialistId);
    }
}