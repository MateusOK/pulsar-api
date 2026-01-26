package com.soupulsar.application.usecase.specialist;

import com.soupulsar.application.dto.response.SpecialistDetailsResponse;
import com.soupulsar.domain.repository.SessionRepository;
import com.soupulsar.domain.repository.SpecialistProfileRepository;
import com.soupulsar.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@RequiredArgsConstructor
public class GetSpecialistDetailsUseCase {

    private final SpecialistProfileRepository specialistRepository;
    private final UserRepository userRepository;
    private final SessionRepository sessionRepository;

    public SpecialistDetailsResponse execute(UUID specialistId) {

        var specialistProfile = specialistRepository.findById(specialistId)
                .orElseThrow(() -> new IllegalArgumentException("Specialist profile not found"));

        var user = userRepository.findById(specialistProfile.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        Long completedSessions = sessionRepository.countCompletedSessionsBySpecialistId(specialistId);

        return SpecialistDetailsResponse.builder()
                .id(specialistProfile.getUserId())
                .name(user.getName())
                .registrationNumber(specialistProfile.getRegistrationNumber())
                .city(user.getAddress().getCity())
                .state(user.getAddress().getState())
                .specialistType(specialistProfile.getSpecialistType())
                .presentation(specialistProfile.getPresentation())
                .sessionsCompleted(completedSessions)
                .price(specialistProfile.getSessionPrice())
                .build();
    }
}