package com.soupulsar.application.specialist.availability;

import com.soupulsar.application.utils.SecurityUtils;
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
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GetSpecialistAvailabilitiesUseCaseTest {

    @Mock
    private AvailabilityRepository repository;

    @Mock
    private SecurityUtils securityUtils;

    @InjectMocks
    private GetSpecialistAvailabilitiesUseCase useCase;

    @Test
    @DisplayName("Should return availabilities for the current specialist")
    void shouldReturnAvailabilitiesForCurrentSpecialist() {
        UUID specialistId = UUID.randomUUID();

        when(securityUtils.getCurrentUserId())
                .thenReturn(specialistId);

        Availability availability = Availability.create(
                specialistId,
                DayOfWeek.MONDAY,
                LocalTime.of(9, 0),
                LocalTime.of(10, 0)
        );

        when(repository.findBySpecialistId(specialistId))
                .thenReturn(List.of(availability));

        GetSpecialistAvailabilitiesResponse response = useCase.execute();

        assertEquals(1, response.availabilities().size());
    }
}