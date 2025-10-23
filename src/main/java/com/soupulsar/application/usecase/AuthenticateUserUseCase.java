package com.soupulsar.application.usecase;

import com.soupulsar.application.dto.request.AuthUserRequest;
import com.soupulsar.application.dto.response.AuthUserResponse;
import com.soupulsar.application.security.JwtService;
import com.soupulsar.application.security.PasswordHasher;
import com.soupulsar.domain.model.user.User;
import com.soupulsar.domain.model.enums.UserStatus;
import com.soupulsar.domain.repository.UserRepository;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;

import java.util.Date;

@RequiredArgsConstructor
public class AuthenticateUserUseCase {

    private final UserRepository userRepository;
    private final PasswordHasher passwordHasher;
    private final JwtService jwtService;

    public AuthUserResponse execute(AuthUserRequest request) {

        User user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new IllegalArgumentException("Invalid email or password"));

        if (user.getStatus() != UserStatus.ACTIVE) {
            throw new IllegalArgumentException("User is not active");
        }

        if (!passwordHasher.matches(request.password(), user.getPasswordHash())) {
            throw new IllegalArgumentException("Invalid email or password");
        }

        var token = jwtService.generateToken(user);
        Date issuedAt = jwtService.extractClaim(token, Claims::getIssuedAt);
        String subject = jwtService.extractClaim(token, Claims::getSubject);
        Long expiresIn = (jwtService.extractClaim(token, Claims::getExpiration).getTime() - System.currentTimeMillis()) / 1000;

        return new AuthUserResponse(
                token,
                "Bearer",
                subject,
                issuedAt,
                expiresIn
        );
    }
}