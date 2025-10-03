package com.soupulsar.modulith.auth.application.usecase;

import com.soupulsar.modulith.auth.application.dto.AuthUserRequest;
import com.soupulsar.modulith.auth.application.security.JwtService;
import com.soupulsar.modulith.auth.application.security.PasswordHasher;
import com.soupulsar.modulith.auth.domain.model.User;
import com.soupulsar.modulith.auth.domain.model.enums.UserStatus;
import com.soupulsar.modulith.auth.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class AuthenticateUserUseCase {

    private final UserRepository userRepository;
    private final PasswordHasher passwordHasher;
    private final JwtService jwtService;

    public String execute(AuthUserRequest request) {

        User user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new IllegalArgumentException("Invalid email or password"));

        if (user.getStatus() != UserStatus.ACTIVE) {
            throw new IllegalArgumentException("User is not active");
        }

        if (!passwordHasher.matches(request.password(), user.getPasswordHash())) {
            throw new IllegalArgumentException("Invalid email or password");
        }

        return jwtService.generateToken(user);

    }
}