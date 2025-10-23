package com.soupulsar.application.usecase;

import com.soupulsar.application.dto.request.RegistrationRequest;
import com.soupulsar.application.dto.response.RegistrationResponse;
import com.soupulsar.application.security.PasswordHasher;
import com.soupulsar.domain.model.client.ClientProfile;
import com.soupulsar.domain.model.specialist.SpecialistProfile;
import com.soupulsar.domain.model.user.User;
import com.soupulsar.domain.model.enums.UserRole;
import com.soupulsar.domain.repository.ClientProfileRepository;
import com.soupulsar.domain.repository.SpecialistProfileRepository;
import com.soupulsar.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;


@RequiredArgsConstructor
public class RegistrationUseCase {

    private final UserRepository userRepository;
    private final ClientProfileRepository clientProfileRepository;
    private final SpecialistProfileRepository specialistProfileRepository;
    private final PasswordHasher passwordHasher;

    @Transactional
    public RegistrationResponse execute(RegistrationRequest request) {

        if(userRepository.existsByEmail(request.email()) || userRepository.existsByCpf(request.cpf())) {
            throw new IllegalArgumentException("Email or CPF already exists");
        }

        User user = User.create(
                request.name(),
                normalizeDigits(request.cpf()),
                normalizeDigits(request.telephone()),
                request.email(),
                passwordHasher.hash(request.password()),
                request.role(),
                request.address().withZipCode(normalizeDigits(request.address().getZipCode()))
        );
        userRepository.save(user);

        if (request.role() == UserRole.CLIENT) {
            ClientProfile clientProfile = ClientProfile.create(
                    user.getUserId(),
                    request.dateOfBirth(),
                    request.emergencyContact().withPhoneNumber(normalizeDigits(request.emergencyContact().getPhoneNumber()))
            );
            clientProfileRepository.save(clientProfile);

        } else if (request.role() == UserRole.SPECIALIST) {
            SpecialistProfile specialistProfile = SpecialistProfile.create(
                    user.getUserId(),
                    request.registrationNumber(),
                    request.presentation(),
                    request.formations(),
                    request.specialties(),
                    request.approaches()
            );
            specialistProfileRepository.save(specialistProfile);
        } else {
            throw new IllegalArgumentException("Invalid user role");
        }

        return new RegistrationResponse(user.getUserId());

    }


    private String normalizeDigits(String input) {
        return input != null ? input.replaceAll("\\D", "") : null;
    }

}