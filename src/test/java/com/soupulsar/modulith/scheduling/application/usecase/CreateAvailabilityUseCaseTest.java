package com.soupulsar.modulith.scheduling.application.usecase;

import com.soupulsar.application.dto.request.CreateAvailabilityRequest;
import com.soupulsar.application.dto.response.CreateAvailabilityResponse;
import com.soupulsar.application.usecase.availability.CreateAvailabilityUseCase;
import com.soupulsar.domain.model.availability.Availability;
import com.soupulsar.domain.repository.AvailabilityRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CreateAvailabilityUseCaseTest {

    private AvailabilityRepository repository;
    private CreateAvailabilityUseCase useCase;

    @BeforeEach
    void setUp() {
        repository = mock(AvailabilityRepository.class);
        useCase = new CreateAvailabilityUseCase(repository);
    }

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

        Availability savedAvailability = new Availability(
                UUID.randomUUID(),
                specialistId,
                DayOfWeek.MONDAY,
                LocalTime.of(9, 0),
                LocalTime.of(17, 0)
        );

        when(repository.save(any())).thenReturn(savedAvailability);

        CreateAvailabilityResponse response = useCase.execute(request);

        assertNotNull(response);
        assertEquals(savedAvailability.getId(), response.id());
        assertEquals(specialistId, response.specialistId());
        assertEquals(DayOfWeek.MONDAY, response.dayOfWeek());
        assertEquals(LocalTime.of(9, 0), response.startTime());
        assertEquals(LocalTime.of(17, 0), response.endTime());

        ArgumentCaptor<Availability> captor = ArgumentCaptor.forClass(Availability.class);
        verify(repository).save(captor.capture());
        assertEquals(specialistId, captor.getValue().getSpecialistId());
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

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            useCase.execute(request);
        });

        assertEquals("End time must be after start time", exception.getMessage());
        verify(repository, never()).save(any());
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

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            useCase.execute(request);
        });

        assertEquals("End time must be after start time", exception.getMessage());
        verify(repository, never()).save(any());
    }
}
