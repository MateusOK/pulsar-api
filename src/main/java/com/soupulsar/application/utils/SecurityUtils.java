package com.soupulsar.application.utils;

import com.soupulsar.domain.exceptions.UserNotFoundException;
import com.soupulsar.domain.model.user.User;
import com.soupulsar.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.UUID;

@RequiredArgsConstructor
public class SecurityUtils {

    private final UserRepository userRepository;

    public UUID getCurrentUserId() {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) {
            throw new AuthenticationException("No authenticated user found") {
            };
        }
        try {
            return UUID.fromString(auth.getName());
        } catch (Exception e) {
            throw new UserNotFoundException("Invalid user ID in authentication context");
        }
    }

    public User getCurrentUser() {
        UUID userId = getCurrentUserId();
        return userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));
    }
}