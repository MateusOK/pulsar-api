package com.soupulsar.application.specialist.block;

import com.soupulsar.domain.exceptions.AvailabilityBlockNotFoundException;
import com.soupulsar.domain.model.availability.AvailabilityBlock;
import com.soupulsar.domain.repository.AvailabilityBlockRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DeleteAvailabilityBlockUseCaseTest {

    @Mock
    private AvailabilityBlockRepository availabilityBlockRepository;

    @InjectMocks
    private DeleteAvailabilityBlockUseCase useCase;

    @Test
    @DisplayName("Should delete availability block successfully when it exists")
    void shouldDeleteAvailabilityBlockSuccessfully() {
        UUID id = UUID.randomUUID();
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

        when(availabilityBlockRepository.findById(id))
                .thenReturn(Optional.of(block));

        assertDoesNotThrow(() -> useCase.execute(id));

        verify(availabilityBlockRepository).delete(block);
    }

    @Test
    @DisplayName("Should throw exception when availability block does not exist")
    void shouldThrowExceptionWhenAvailabilityBlockDoesNotExist() {
        UUID id = UUID.randomUUID();

        when(availabilityBlockRepository.findById(id))
                .thenReturn(Optional.empty());

        assertThrows(
                AvailabilityBlockNotFoundException.class,
                () -> useCase.execute(id)
        );
    }
}