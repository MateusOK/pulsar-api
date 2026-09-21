package com.soupulsar.application.session;

import com.soupulsar.application.dto.request.ScheduleSessionRequest;
import com.soupulsar.application.dto.response.ScheduleSessionResponse;
import com.soupulsar.application.session.shared.SessionAvailabilityChecker;
import com.soupulsar.domain.model.session.Session;
import com.soupulsar.domain.repository.SessionRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ScheduleSessionUseCase {

    private final SessionRepository sessionRepository;
    private final SessionAvailabilityChecker sessionAvailabilityChecker;

    public ScheduleSessionResponse execute(ScheduleSessionRequest request) {

        sessionAvailabilityChecker.validate(request.specialistId(), request.startTime(), request.endTime(), null);

        Session savedSession = sessionRepository.save(Session.scheduleSession(
                request.specialistId(),
                request.clientId(),
                request.startTime(),
                request.endTime()
        ));

        return new ScheduleSessionResponse(savedSession);
    }
}