package com.soupulsar.application.usecase;

import com.soupulsar.application.dto.request.UpdateUserProfileRequest;
import com.soupulsar.application.utils.SecurityUtils;
import com.soupulsar.domain.repository.ClientProfileRepository;
import com.soupulsar.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
public class UpdateUserProfileUseCase {


    private final UserRepository userRepository;
    private final ClientProfileRepository clientProfileRepository;
    private final SecurityUtils securityUtils;

    @Transactional
    public void execute(UpdateUserProfileRequest request){

        var user = securityUtils.getCurrentUser();

        var clientProfile = clientProfileRepository.findById(user.getUserId())
                    .orElseThrow(() -> new IllegalArgumentException("Client profile not found"));

        if (request.email() != null && !request.email().equals(user.getEmail()) && userRepository.existsByEmail(request.email())) {
                throw new IllegalArgumentException("Email already exists");
            }
        user.setEmail(request.email());
        user.setAddress(request.address() != null ? request.address() : user.getAddress());
        user.setName(request.name() != null ? request.name() : user.getName());
        user.setTelephone(request.phone() != null ? request.phone() : user.getTelephone());

        clientProfile.setEmergencyContact(request.emergencyContact() != null ? request.emergencyContact() : clientProfile.getEmergencyContact());

        userRepository.save(user);
        clientProfileRepository.save(clientProfile);
    }
}