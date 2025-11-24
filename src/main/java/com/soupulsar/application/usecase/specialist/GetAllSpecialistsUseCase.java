package com.soupulsar.application.usecase.specialist;

import com.soupulsar.application.dto.request.GetAllSpecialistRequest;
import com.soupulsar.application.dto.response.GetAllSpecialistsResponse;
import com.soupulsar.domain.model.specialist.SpecialistProfile;
import com.soupulsar.domain.model.user.User;
import com.soupulsar.domain.repository.SessionRepository;
import com.soupulsar.domain.repository.SpecialistProfileRepository;
import com.soupulsar.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RequiredArgsConstructor
public class GetAllSpecialistsUseCase {

    private final SpecialistProfileRepository specialistProfileRepository;
    private final UserRepository userRepository;
    private final SessionRepository sessionRepository;


    public Page<GetAllSpecialistsResponse> execute(GetAllSpecialistRequest request) {

        Page<SpecialistProfile> specialistProfiles =
                specialistProfileRepository.findAll(request.specialistType(), request.minPrice(), request.maxPrice(), request.toPageable());


        List<UUID> userIds = specialistProfiles
                .getContent()
                .stream()
                .map(SpecialistProfile::getUserId)
                .toList();

        Map<UUID, User> userMap = userRepository.findAllByUserId(userIds);

        Map<UUID, Long> sessionCompletedCountMap =
                sessionRepository.countCompletedSessionsBySpecialistIds(userIds);

        return specialistProfiles.map(specialist ->{
            User user = userMap.get(specialist.getUserId());

            return new GetAllSpecialistsResponse(
                    specialist.getUserId(),
                    user.getName(),
                    user.getAddress().getCity(),
                    user.getAddress().getState(),
                    specialist.getSpecialties(),
                    specialist.getApproaches(),
                    specialist.getPresentation().getBase64Image(),
                    specialist.getSpecialistType(),
                    specialist.getSessionPrice(),
                    sessionCompletedCountMap.getOrDefault(specialist.getUserId(), 0L)
            );

        });

    }

}