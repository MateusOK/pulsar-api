package com.soupulsar.application.usecase.auth;

import com.soupulsar.application.dto.request.ResetPasswordRequest;
import com.soupulsar.domain.exceptions.ExpiredPasswordResetTokenException;
import com.soupulsar.domain.exceptions.InvalidPasswordResetTokenException;
import com.soupulsar.domain.exceptions.UserNotFoundException;
import com.soupulsar.domain.model.auth.PasswordResetToken;
import com.soupulsar.domain.model.user.User;
import com.soupulsar.domain.repository.PasswordResetTokenRepository;
import com.soupulsar.domain.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;

@Slf4j
@RequiredArgsConstructor
public class ResetPasswordUseCase {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final PasswordResetTokenRepository passwordResetTokenRepository;

    @Transactional
    public void execute(ResetPasswordRequest request) {

        PasswordResetToken resetToken = passwordResetTokenRepository.findByToken(request.token())
                .orElseThrow(InvalidPasswordResetTokenException::new);

        if (!resetToken.isUsable()) {
            throw new ExpiredPasswordResetTokenException();
        }

        User user = userRepository.findById(resetToken.getUserId())
                .orElseThrow(UserNotFoundException::new);

        user.setPasswordHash(passwordEncoder.encode(request.newPassword()));

        resetToken.markUsed();

        userRepository.save(user);
        passwordResetTokenRepository.save(resetToken);

        log.info("Password reset successful for user {}", user.getUserId());
    }
}