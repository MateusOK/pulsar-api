package com.soupulsar.application.client;

import com.soupulsar.application.utils.SecurityUtils;
import com.soupulsar.domain.exceptions.UserNotFoundException;
import com.soupulsar.domain.model.session.Session;
import com.soupulsar.domain.model.user.User;
import com.soupulsar.domain.repository.SessionRepository;
import com.soupulsar.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
public class GetClientNextAppointmentUseCase {

    private final Clock clock;
    private final SecurityUtils securityUtils;
    private final UserRepository userRepository;
    private final SessionRepository sessionRepository;

    public HomeResponse execute() {

        UUID clientId = securityUtils.getCurrentUserId();

        LocalDateTime now = LocalDateTime.now(clock);

        var nextAppointment = getNextAppointment(clientId, now);

        return HomeResponse.builder()
                .nextAppointment(nextAppointment)
                .build();
    }

    private ClientNextAppointmentResponse getNextAppointment(UUID clientId, LocalDateTime currentDateTime) {

        Optional<Session> optionalSession = sessionRepository.findNextClientSession(clientId, currentDateTime);

        Session session = optionalSession.orElse(null);

        if (session == null) {
            log.info("No session found for client {}", clientId);
            return null;
        }

        User specialist = userRepository.findById(session.getSpecialistId())
                .orElseThrow(() -> new UserNotFoundException("user not found"));

        return ClientNextAppointmentResponse.builder()
                .id(session.getSessionId())
                .startsAt(session.getStartAt())
                .endsAt(session.getEndAt())
                .specialistName(specialist.getName())
                .build();
    }
}