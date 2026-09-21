package com.soupulsar.application.session;

import com.soupulsar.application.dto.request.RescheduleSessionRequest;
import com.soupulsar.application.dto.response.ScheduleSessionResponse;
import com.soupulsar.application.session.shared.SessionAvailabilityChecker;
import com.soupulsar.application.utils.SecurityUtils;
import com.soupulsar.domain.exceptions.SessionNotFoundException;
import com.soupulsar.domain.model.session.Session;
import com.soupulsar.domain.repository.SessionRepository;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@RequiredArgsConstructor
public class RescheduleSessionUseCase {

    private final SecurityUtils securityUtils;
    private final SessionRepository sessionRepository;
    private final SessionAvailabilityChecker sessionAvailabilityChecker;

    public ScheduleSessionResponse execute(RescheduleSessionRequest request) {

        UUID userId = securityUtils.getCurrentUserId();

        Session session = sessionRepository.findBySessionId(request.sessionId())
                .orElseThrow(() -> new SessionNotFoundException(request.sessionId()));

        if (!session.getSpecialistId().equals(userId)){
            throw new IllegalArgumentException("You are not authorized to reschedule this session");
        }

        sessionAvailabilityChecker.validate(session.getSpecialistId(), request.startTime(), request.endTime(), session.getSessionId());

        session.rescheduleSession(request.startTime(), request.endTime());

        return new ScheduleSessionResponse(sessionRepository.save(session));
    }
}