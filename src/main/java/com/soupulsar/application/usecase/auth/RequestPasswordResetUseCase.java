package com.soupulsar.application.usecase.auth;

import com.soupulsar.application.dto.request.ResetPasswordEmailRequest;
import com.soupulsar.application.interfaces.EmailGateway;
import com.soupulsar.application.interfaces.TokenGenerator;
import com.soupulsar.domain.exceptions.UserNotFoundException;
import com.soupulsar.domain.model.auth.PasswordResetToken;
import com.soupulsar.domain.model.user.User;
import com.soupulsar.domain.repository.PasswordResetTokenRepository;
import com.soupulsar.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;

import java.time.Duration;
import java.time.Instant;

@Slf4j
@RequiredArgsConstructor
public class RequestPasswordResetUseCase {

    private final UserRepository userRepository;
    private final TokenGenerator tokenGenerator;
    private final PasswordResetTokenRepository passwordResetTokenRepository;
    private final EmailGateway emailGateway;
    private static final Duration TOKEN_EXPIRATION = Duration.ofHours(1);

    private final String passwordResetUrl;

    public void execute(ResetPasswordEmailRequest request) {
        log.info("Received email: '{}'", request.email());
        User user = userRepository.findByEmail(request.email()).orElseThrow(UserNotFoundException::new);

        String token = tokenGenerator.generate();

        PasswordResetToken passwordResetToken = PasswordResetToken.create(
                user.getUserId(),
                token,
                Instant.now().plus(TOKEN_EXPIRATION)
        );

        passwordResetTokenRepository.save(passwordResetToken);

        String resetLink = passwordResetUrl + "/reset-password/" + token;

        emailGateway.sendPasswordResetEmail(resetLink, user.getName(), user.getEmail());

    }
}