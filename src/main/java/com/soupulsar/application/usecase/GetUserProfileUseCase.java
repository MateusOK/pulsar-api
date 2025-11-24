package com.soupulsar.application.usecase;

import com.soupulsar.application.dto.response.AddressResponse;
import com.soupulsar.application.dto.response.ClientProfileResponse;
import com.soupulsar.application.dto.response.SpecialistProfileResponse;
import com.soupulsar.application.dto.response.UserProfileResponse;
import com.soupulsar.application.utils.SecurityUtils;
import com.soupulsar.domain.model.client.ClientProfile;
import com.soupulsar.domain.model.specialist.SpecialistProfile;
import com.soupulsar.domain.model.user.User;
import com.soupulsar.domain.repository.ClientProfileRepository;
import com.soupulsar.domain.repository.SpecialistProfileRepository;
import com.soupulsar.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.UUID;

@RequiredArgsConstructor
public class GetUserProfileUseCase {

    private final ClientProfileRepository clientProfileRepository;
    private final SpecialistProfileRepository specialistProfileRepository;
    private final UserRepository userRepository;
    private final SecurityUtils securityUtils;


    public UserProfileResponse execute(){

        UUID userId = securityUtils.getCurrentUserId();

        User user = userRepository.findById(userId).orElseThrow(() -> new UsernameNotFoundException("User not found"));

        return switch (user.getRole()){
            case CLIENT -> buildClientResponse(userId, user);
            case SPECIALIST -> buildSpecialistResponse(userId, user);
            case ADMIN -> throw new UnsupportedOperationException("Admin profile retrieval is not supported");
        };

    }

    private SpecialistProfileResponse buildSpecialistResponse(UUID userId, User user){
        SpecialistProfile specialistProfile = specialistProfileRepository.findById(userId).
                orElseThrow(() -> new UsernameNotFoundException("Specialist not found with id: " + user.getUserId()));

        return SpecialistProfileResponse.builder()
                .name(user.getName())
                .email(user.getEmail())
                .telephone(user.getTelephone())
                .specialistType(specialistProfile.getSpecialistType())
                .therapeuticApproaches(specialistProfile.getApproaches())
                .clinicalSpecialties(specialistProfile.getSpecialties())
                .education(specialistProfile.getFormations())
                .addressResponse(AddressResponse.toResponse(user.getAddress()))
                .build();
    }


    private ClientProfileResponse buildClientResponse(UUID userId, User user) {
        ClientProfile clientProfile = clientProfileRepository.findById(userId).
                orElseThrow(() -> new UsernameNotFoundException("Client not found with id: " + user.getUserId()));

        return ClientProfileResponse.builder()
                .name(user.getName())
                .email(user.getEmail())
                .telephone(user.getTelephone())
                .birthday(clientProfile.getDateOfBirth())
                .addressResponse(AddressResponse.toResponse(user.getAddress()))
                .emergencyContact(clientProfile.getEmergencyContact())
                .build();

    }
}