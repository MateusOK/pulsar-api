package com.soupulsar.application.usecase;

import com.soupulsar.application.dto.response.UserResponse;
import com.soupulsar.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@RequiredArgsConstructor
public class GetUserByIdUseCase {

    private final UserRepository userRepository;

    public UserResponse execute(UUID userId) {

        return new UserResponse(userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found")));
    }
}