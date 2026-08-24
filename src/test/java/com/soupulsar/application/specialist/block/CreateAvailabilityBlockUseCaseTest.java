package com.soupulsar.application.specialist.block;

import com.soupulsar.domain.exceptions.FutureSessionConflictException;
import com.soupulsar.domain.model.availability.AvailabilityBlock;
import com.soupulsar.domain.repository.AvailabilityBlockRepository;
import com.soupulsar.domain.repository.SessionRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CreateAvailabilityBlockUseCaseTest {

    @Mock
    private AvailabilityBlockRepository availabilityBlockRepository;

    @Mock
    private SessionRepository sessionRepository;

    @InjectMocks
    private CreateAvailabilityBlockUseCase useCase;

    @Test
    @DisplayName("Should create availability block successfully when there are no conflicting future sessions")
    void shouldCreateAvailabilityBlockSuccessfully() {
        UUID specialistId = UUID.randomUUID();
        LocalDateTime startsAt = LocalDateTime.of(2026, 8, 21, 9, 0);
        LocalDateTime endsAt = LocalDateTime.of(2026, 8, 21, 10, 0);
        String reason = "Personal appointment";

        CreateAvailabilityBlockRequest request =
                new CreateAvailabilityBlockRequest(
                        specialistId,
                        startsAt,
                        endsAt,
                        reason
                );

        AvailabilityBlock block =
                AvailabilityBlock.create(
                        specialistId,
                        startsAt,
                        endsAt,
                        reason
                );

        when(sessionRepository.countConflictingFutureSessionsForAvailabilityBlocks(
                specialistId,
                startsAt,
                endsAt
        )).thenReturn(0L);

        when(availabilityBlockRepository.save(any(AvailabilityBlock.class)))
                .thenReturn(block);

        CreateAvailabilityBlockResponse response = useCase.execute(request);

        assertNotNull(response);
        assertEquals(specialistId, response.specialistId());
        assertEquals(startsAt, response.startsAt());
        assertEquals(endsAt, response.endsAt());

        verify(availabilityBlockRepository).save(any(AvailabilityBlock.class));
    }

    @Test
    @DisplayName("Should throw exception when availability block conflicts with future sessions")
    void shouldThrowExceptionWhenAvailabilityBlockConflictsWithFutureSessions() {
        UUID specialistId = UUID.randomUUID();
        LocalDateTime startsAt = LocalDateTime.of(2026, 8, 21, 9, 0);
        LocalDateTime endsAt = LocalDateTime.of(2026, 8, 21, 10, 0);
        String reason = "Personal appointment";

        CreateAvailabilityBlockRequest request =
                new CreateAvailabilityBlockRequest(
                        specialistId,
                        startsAt,
                        endsAt,
                        reason
                );

        when(sessionRepository.countConflictingFutureSessionsForAvailabilityBlocks(
                specialistId,
                startsAt,
                endsAt
        )).thenReturn(2L);

        assertThrows(
                FutureSessionConflictException.class,
                () -> useCase.execute(request)
        );

        verify(availabilityBlockRepository, never())
                .save(any(AvailabilityBlock.class));
    }
}