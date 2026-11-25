package com.soupulsar.application.client;

import com.soupulsar.application.utils.SecurityUtils;
import com.soupulsar.domain.exceptions.UserNotFoundException;
import com.soupulsar.domain.model.session.Session;
import com.soupulsar.domain.model.specialist.SpecialistProfile;
import com.soupulsar.domain.model.user.User;
import com.soupulsar.domain.repository.SessionRepository;
import com.soupulsar.domain.repository.SpecialistProfileRepository;
import com.soupulsar.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RequiredArgsConstructor
public class GetClientSessionsUseCase {

    private final Clock clock;
    private final SecurityUtils securityUtils;
    private final SessionRepository sessionRepository;
    private final UserRepository userRepository;
    private final SpecialistProfileRepository specialistProfileRepository;

    public ClientSessionsResponse execute() {
        UUID clientId = securityUtils.getCurrentUserId();
        LocalDateTime now = LocalDateTime.now(clock);

        List<Session> sessions = sessionRepository.findByClientId(clientId);

        if (sessions.isEmpty()) {
            return ClientSessionsResponse.builder()
                    .futureSessions(List.of())
                    .history(List.of())
                    .build();
        }

        List<UUID> specialistIds = sessions.stream()
                .map(Session::getSpecialistId)
                .distinct()
                .toList();

        Map<UUID, User> specialists = userRepository.findAllByUserId(specialistIds);
        Map<UUID, SpecialistProfile> specialistProfiles = specialistProfileRepository.findAllByUserId(specialistIds);

        List<ClientSessionResponse> futureSessions = sessions.stream()
                .filter(session -> !session.getStartAt().isBefore(now))
                .sorted(Comparator.comparing(Session::getStartAt))
                .map(session -> toResponse(session, specialists, specialistProfiles))
                .toList();

        List<ClientSessionResponse> history = sessions.stream()
                .filter(session -> session.getStartAt().isBefore(now))
                .sorted(Comparator.comparing(Session::getStartAt).reversed())
                .map(session -> toResponse(session, specialists, specialistProfiles))
                .toList();

        return ClientSessionsResponse.builder()
                .futureSessions(futureSessions)
                .history(history)
                .build();
    }

    private ClientSessionResponse toResponse(
            Session session,
            Map<UUID, User> specialists,
            Map<UUID, SpecialistProfile> specialistProfiles
    ) {
        UUID specialistId = session.getSpecialistId();

        User specialist = specialists.get(specialistId);
        if (specialist == null) {
            throw new UserNotFoundException(specialistId);
        }

        SpecialistProfile profile = specialistProfiles.get(specialistId);
        if (profile == null) {
            throw new UserNotFoundException("Specialist profile not found with ID: " + specialistId);
        }

        return ClientSessionResponse.builder()
                .sessionId(session.getSessionId())
                .startsAt(session.getStartAt())
                .endsAt(session.getEndAt())
                .status(session.getStatus())
                .specialistId(specialistId)
                .specialistName(specialist.getName())
                .registrationNumber(profile.getRegistrationNumber())
                .specialistType(profile.getSpecialistType())
                .build();
    }
}
