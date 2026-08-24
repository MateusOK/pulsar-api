package com.soupulsar.application.specialist.availability;

import com.soupulsar.domain.exceptions.AvailabilityOverlapException;
import com.soupulsar.domain.model.availability.Availability;
import com.soupulsar.domain.repository.AvailabilityRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.Collections;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CreateAvailabilityUseCaseTest {

    @Mock
    private AvailabilityRepository repository;

    @InjectMocks
    private CreateAvailabilityUseCase useCase;

    @Test
    @DisplayName("Should create availability successfully when times are valid")
    void shouldCreateAvailabilitySuccessfully() {
        UUID specialistId = UUID.randomUUID();
        CreateAvailabilityRequest request = new CreateAvailabilityRequest(
                specialistId,
                DayOfWeek.MONDAY,
                LocalTime.of(9, 0),
                LocalTime.of(17, 0)
        );

        when(repository.findBySpecialistIdAndDayOfWeek(
                specialistId,
                DayOfWeek.MONDAY
        )).thenReturn(Collections.emptyList());

        Availability savedAvailability = Availability.restore(
                UUID.randomUUID(),
                specialistId,
                DayOfWeek.MONDAY,
                true,
                LocalTime.of(9, 0),
                LocalTime.of(17, 0)
        );

        when(repository.save(any(Availability.class)))
                .thenReturn(savedAvailability);

        CreateAvailabilityResponse response = useCase.execute(request);

        assertNotNull(response);
        assertEquals(savedAvailability.getId(), response.id());
        assertEquals(specialistId, response.specialistId());
        assertEquals(DayOfWeek.MONDAY, response.dayOfWeek());
        assertEquals(LocalTime.of(9, 0), response.startTime());
        assertEquals(LocalTime.of(17, 0), response.endTime());

        verify(repository).save(any(Availability.class));
    }

    @Test
    @DisplayName("Should throw exception when availability overlaps an existing availability")
    void shouldThrowExceptionWhenAvailabilityOverlapsExistingAvailability() {
        UUID specialistId = UUID.randomUUID();
        CreateAvailabilityRequest request = new CreateAvailabilityRequest(
                specialistId,
                DayOfWeek.MONDAY,
                LocalTime.of(9, 0),
                LocalTime.of(10, 0)
        );

        Availability existingAvailability = Availability.create(
                specialistId,
                DayOfWeek.MONDAY,
                LocalTime.of(9, 30),
                LocalTime.of(10, 30)
        );

        when(repository.findBySpecialistIdAndDayOfWeek(
                specialistId,
                DayOfWeek.MONDAY
        )).thenReturn(Collections.singletonList(existingAvailability));

        assertThrows(
                AvailabilityOverlapException.class,
                () -> useCase.execute(request)
        );

        verify(repository, never()).save(any(Availability.class));
    }

    @Test
    @DisplayName("Should throw exception when end time is before start time")
    void shouldThrowExceptionWhenEndTimeIsBeforeStartTime() {
        CreateAvailabilityRequest request = new CreateAvailabilityRequest(
                UUID.randomUUID(),
                DayOfWeek.TUESDAY,
                LocalTime.of(10, 0),
                LocalTime.of(9, 0)
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> useCase.execute(request)
        );

        verify(repository, never()).save(any(Availability.class));
    }

    @Test
    @DisplayName("Should throw exception when end time equals start time")
    void shouldThrowExceptionWhenEndTimeEqualsStartTime() {
        CreateAvailabilityRequest request = new CreateAvailabilityRequest(
                UUID.randomUUID(),
                DayOfWeek.WEDNESDAY,
                LocalTime.of(10, 0),
                LocalTime.of(10, 0)
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> useCase.execute(request)
        );

        verify(repository, never()).save(any(Availability.class));
    }
}