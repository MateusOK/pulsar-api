package com.soupulsar.application.specialist.availability;

import com.soupulsar.application.utils.SecurityUtils;
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
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UpdateAvailabilityDayUseCaseTest {

    @Mock
    private AvailabilityRepository repository;

    @Mock
    private SessionRepository sessionRepository;

    @Mock
    private SecurityUtils securityUtils;

    @InjectMocks
    private UpdateAvailabilityDayUseCase useCase;

    @Test
    @DisplayName("Should enable availability day successfully")
    void shouldEnableAvailabilityDaySuccessfully() {
        UUID specialistId = UUID.randomUUID();

        when(securityUtils.getCurrentUserId())
                .thenReturn(specialistId);

        Availability availability = Availability.create(
                specialistId,
                DayOfWeek.MONDAY,
                LocalTime.of(9, 0),
                LocalTime.of(10, 0)
        );

        when(repository.findBySpecialistIdAndDayOfWeek(
                specialistId,
                DayOfWeek.MONDAY
        )).thenReturn(List.of(availability));

        UpdateAvailabilityDayRequest request =
                new UpdateAvailabilityDayRequest(DayOfWeek.MONDAY, true);

        assertDoesNotThrow(() -> useCase.execute(request));
    }

    @Test
    @DisplayName("Should throw exception when disabling a day with future sessions")
    void shouldThrowExceptionWhenDisablingDayWithFutureSessions() {
        UUID specialistId = UUID.randomUUID();

        when(securityUtils.getCurrentUserId())
                .thenReturn(specialistId);

        Availability availability = Availability.create(
                specialistId,
                DayOfWeek.MONDAY,
                LocalTime.of(9, 0),
                LocalTime.of(10, 0)
        );

        when(repository.findBySpecialistIdAndDayOfWeek(
                specialistId,
                DayOfWeek.MONDAY
        )).thenReturn(List.of(availability));

        when(sessionRepository.countConflictingFutureSessionsForAvailability(
                specialistId,
                availability.getDayOfWeek(),
                availability.getStartTime(),
                availability.getEndTime()
        )).thenReturn(1L);

        UpdateAvailabilityDayRequest request =
                new UpdateAvailabilityDayRequest(DayOfWeek.MONDAY, false);

        assertThrows(
                FutureSessionConflictException.class,
                () -> useCase.execute(request)
        );
    }
}