package com.soupulsar.application.usecase.specialist;

import com.soupulsar.application.dto.response.DailyAvailabilityResponse;
import com.soupulsar.application.dto.response.TimeSlot;
import com.soupulsar.domain.model.availability.Availability;
import com.soupulsar.domain.model.session.Session;
import com.soupulsar.domain.repository.AvailabilityRepository;
import com.soupulsar.domain.repository.SessionRepository;
import lombok.RequiredArgsConstructor;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
public class GetDailyAvailabilityUseCase {

    private static final Duration SESSION_DURATION = Duration.ofMinutes(50);
    private static final Duration BREAK_INTERVAL = Duration.ofMinutes(10);

    private final SessionRepository sessionRepository;
    private final AvailabilityRepository availabilityRepository;

    public DailyAvailabilityResponse execute(UUID specialistId, LocalDate date) {

        List<Availability> availabilities = availabilityRepository.findBySpecialistIdAndDayOfWeek(specialistId, date.getDayOfWeek());
        if (availabilities.isEmpty()) return new DailyAvailabilityResponse(date, List.of());


        List<Session> sessions = sessionRepository.findBySpecialistIdAndDate(specialistId, date);

        List<TimeSlot> timeSlots = generateTimeSlots(availabilities, sessions);

        return new DailyAvailabilityResponse(date, timeSlots);
    }


    private List<TimeSlot> generateTimeSlots(List<Availability> availabilities, List<Session> sessions) {
        List<TimeSlot> timeSlots = new ArrayList<>();

        for (Availability availability : availabilities) {
            LocalTime current = availability.getStartTime();

            while (!current.plus(SESSION_DURATION).isAfter(availability.getEndTime())) {
                LocalTime slotStart = current;
                LocalTime slotEnd = current.plus(SESSION_DURATION);

                boolean isBooked = sessions.stream().anyMatch(session -> {
                    LocalTime sessionStart = session.getStartAt().toLocalTime();
                    LocalTime sessionEnd = session.getEndAt().toLocalTime();

                    LocalTime sessionStartWithBuffer = sessionStart.minus(BREAK_INTERVAL);
                    LocalTime sessionEndWithBuffer = sessionEnd.plus(BREAK_INTERVAL);

                    return slotStart.isBefore(sessionEndWithBuffer) && slotEnd.isAfter(sessionStartWithBuffer);
                });

                timeSlots.add(new TimeSlot(slotStart, slotEnd, isBooked));

                current = slotEnd.plus(BREAK_INTERVAL);
            }
        }

        return timeSlots;
    }

}