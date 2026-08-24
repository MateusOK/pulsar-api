package com.soupulsar.application.specialist.availability;

import com.soupulsar.domain.exceptions.AvailabilityNotFoundException;
import com.soupulsar.domain.exceptions.AvailabilityOverlapException;
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
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UpdateAvailabilityUseCaseTest {

    @Mock
    private AvailabilityRepository repository;

    @Mock
    private SessionRepository sessionRepository;

    @InjectMocks
    private UpdateAvailabilityUseCase useCase;

    @Test
    @DisplayName("Should update availability successfully")
    void shouldUpdateAvailabilitySuccessfully() {
        UUID id = UUID.randomUUID();
        UUID specialistId = UUID.randomUUID();

        Availability availability = Availability.restore(
                id,
                specialistId,
                DayOfWeek.MONDAY,
                true,
                LocalTime.of(9, 0),
                LocalTime.of(10, 0)
        );

        when(repository.findById(id))
                .thenReturn(Optional.of(availability));

        when(repository.findBySpecialistIdAndDayOfWeek(
                specialistId,
                DayOfWeek.MONDAY
        )).thenReturn(List.of(availability));

        when(sessionRepository.countConflictingFutureSessionsForAvailability(
                specialistId,
                DayOfWeek.MONDAY,
                LocalTime.of(8, 0),
                LocalTime.of(9, 0)
        )).thenReturn(0L);

        UpdateAvailabilityRequest request =
                new UpdateAvailabilityRequest(
                        LocalTime.of(8, 0),
                        LocalTime.of(9, 0)
                );

        assertDoesNotThrow(() -> useCase.execute(id, request));
    }

    @Test
    @DisplayName("Should throw exception when availability does not exist")
    void shouldThrowExceptionWhenAvailabilityDoesNotExist() {
        UUID id = UUID.randomUUID();

        when(repository.findById(id))
                .thenReturn(Optional.empty());

        UpdateAvailabilityRequest request =
                new UpdateAvailabilityRequest(
                        LocalTime.of(8, 0),
                        LocalTime.of(9, 0)
                );

        assertThrows(
                AvailabilityNotFoundException.class,
                () -> useCase.execute(id, request)
        );
    }

    @Test
    @DisplayName("Should throw exception when updated availability overlaps another availability")
    void shouldThrowExceptionWhenUpdatedAvailabilityOverlapsAnotherAvailability() {
        UUID id = UUID.randomUUID();
        UUID specialistId = UUID.randomUUID();

        Availability availability = Availability.restore(
                id,
                specialistId,
                DayOfWeek.MONDAY,
                true,
                LocalTime.of(9, 0),
                LocalTime.of(10, 0)
        );

        Availability otherAvailability = Availability.create(
                specialistId,
                DayOfWeek.MONDAY,
                LocalTime.of(8, 30),
                LocalTime.of(9, 30)
        );

        when(repository.findById(id))
                .thenReturn(Optional.of(availability));

        when(repository.findBySpecialistIdAndDayOfWeek(
                specialistId,
                DayOfWeek.MONDAY
        )).thenReturn(List.of(availability, otherAvailability));

        UpdateAvailabilityRequest request =
                new UpdateAvailabilityRequest(
                        LocalTime.of(8, 30),
                        LocalTime.of(9, 30)
                );

        assertThrows(
                AvailabilityOverlapException.class,
                () -> useCase.execute(id, request)
        );
    }

    @Test
    @DisplayName("Should throw exception when updated availability conflicts with future sessions")
    void shouldThrowExceptionWhenUpdatedAvailabilityConflictsWithFutureSessions() {
        UUID id = UUID.randomUUID();
        UUID specialistId = UUID.randomUUID();

        Availability availability = Availability.restore(
                id,
                specialistId,
                DayOfWeek.MONDAY,
                true,
                LocalTime.of(9, 0),
                LocalTime.of(10, 0)
        );

        when(repository.findById(id))
                .thenReturn(Optional.of(availability));

        when(repository.findBySpecialistIdAndDayOfWeek(
                specialistId,
                DayOfWeek.MONDAY
        )).thenReturn(List.of(availability));

        when(sessionRepository.countConflictingFutureSessionsForAvailability(
                specialistId,
                DayOfWeek.MONDAY,
                LocalTime.of(8, 0),
                LocalTime.of(9, 0)
        )).thenReturn(1L);

        UpdateAvailabilityRequest request =
                new UpdateAvailabilityRequest(
                        LocalTime.of(8, 0),
                        LocalTime.of(9, 0)
                );

        assertThrows(
                FutureSessionConflictException.class,
                () -> useCase.execute(id, request)
        );
    }
}