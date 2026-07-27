package com.soupulsar.application.specialist.calendar;

import com.soupulsar.application.specialist.shared.WhatsAppLinkGenerator;
import com.soupulsar.application.utils.SecurityUtils;
import com.soupulsar.domain.exceptions.SessionNotFoundException;
import com.soupulsar.domain.exceptions.UserNotFoundException;
import com.soupulsar.domain.model.session.Session;
import com.soupulsar.domain.repository.SessionRepository;
import com.soupulsar.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@RequiredArgsConstructor
public class GetSessionDetailsUseCase {

    private final SecurityUtils securityUtils;
    private final SessionRepository sessionRepository;
    private final UserRepository userRepository;
    private final WhatsAppLinkGenerator whatsAppLinkGenerator;

    public SessionDetailsResponse execute(UUID sessionId){

        var specialistId = securityUtils.getCurrentUserId();

        Session session = sessionRepository.findBySessionIdAndSpecialistId(sessionId, specialistId)
                .orElseThrow(() -> new SessionNotFoundException(specialistId, sessionId));

        var client = userRepository.findById(session.getClientId())
                .orElseThrow(() -> new UserNotFoundException(session.getClientId()));

        return SessionDetailsResponse.builder()
                .sessionId(session.getSessionId())
                .clientId(client.getUserId())
                .clientName(client.getName())
                .clientEmail(client.getEmail())
                .clientPhone(client.getTelephone())
                .startsAt(session.getStartAt())
                .endsAt(session.getEndAt())
                .status(session.getStatus())
                .whatsappUrl(whatsAppLinkGenerator.generate(client.getTelephone()))
                .build();
    }
}