package com.soupulsar.application.usecase;

import com.soupulsar.application.dto.request.ChangePasswordRequest;
import com.soupulsar.application.utils.SecurityUtils;
import com.soupulsar.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@RequiredArgsConstructor
public class ChangePasswordUseCase {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final SecurityUtils securityUtils;

    @Transactional
    public void execute(ChangePasswordRequest request){

        var user = securityUtils.getCurrentUser();

        if (!passwordEncoder.matches(request.currentPassword(), user.getPasswordHash())) {
            throw new IllegalArgumentException("Current password is incorrect");
        }

        user.setPasswordHash(passwordEncoder.encode(request.newPassword()));

        userRepository.save(user);
    }
}