package com.soupulsar.application.specialist.availability;

import com.soupulsar.domain.exceptions.AvailabilityNotFoundException;
import com.soupulsar.domain.exceptions.FutureSessionConflictException;
import com.soupulsar.domain.model.availability.Availability;
import com.soupulsar.domain.repository.AvailabilityRepository;
import com.soupulsar.domain.repository.SessionRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DeleteAvailabilityUseCaseTest {

    @Mock
    private AvailabilityRepository availabilityRepository;

    @Mock
    private SessionRepository sessionRepository;

    @InjectMocks
    private DeleteAvailabilityUseCase useCase;

    @Test
    @DisplayName("Should delete availability successfully when there are no future sessions")
    void shouldDeleteAvailabilitySuccessfully() {
        UUID id = UUID.randomUUID();

        Availability availability = Availability.restore(
                id,
                UUID.randomUUID(),
                DayOfWeek.MONDAY,
                true,
                LocalTime.of(9, 0),
                LocalTime.of(10, 0)
        );

        when(availabilityRepository.findById(id))
                .thenReturn(Optional.of(availability));

        when(sessionRepository.countConflictingFutureSessionsForAvailability(
                availability.getSpecialistId(),
                availability.getDayOfWeek(),
                availability.getStartTime(),
                availability.getEndTime()
        )).thenReturn(0L);

        assertDoesNotThrow(() -> useCase.execute(id));
    }

    @Test
    @DisplayName("Should throw exception when availability does not exist")
    void shouldThrowExceptionWhenAvailabilityDoesNotExist() {
        UUID id = UUID.randomUUID();

        when(availabilityRepository.findById(id))
                .thenReturn(Optional.empty());

        assertThrows(
                AvailabilityNotFoundException.class,
                () -> useCase.execute(id)
        );
    }

    @Test
    @DisplayName("Should throw exception when availability has future sessions")
    void shouldThrowExceptionWhenAvailabilityHasFutureSessions() {
        UUID id = UUID.randomUUID();

        Availability availability = Availability.restore(
                id,
                UUID.randomUUID(),
                DayOfWeek.MONDAY,
                true,
                LocalTime.of(9, 0),
                LocalTime.of(10, 0)
        );

        when(availabilityRepository.findById(id))
                .thenReturn(Optional.of(availability));

        when(sessionRepository.countConflictingFutureSessionsForAvailability(
                availability.getSpecialistId(),
                availability.getDayOfWeek(),
                availability.getStartTime(),
                availability.getEndTime()
        )).thenReturn(2L);

        assertThrows(
                FutureSessionConflictException.class,
                () -> useCase.execute(id)
        );
    }
}