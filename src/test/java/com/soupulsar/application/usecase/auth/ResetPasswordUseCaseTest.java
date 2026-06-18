package com.soupulsar.application.usecase.auth;

import com.soupulsar.application.dto.request.ResetPasswordRequest;
import com.soupulsar.domain.exceptions.ExpiredPasswordResetTokenException;
import com.soupulsar.domain.exceptions.InvalidPasswordResetTokenException;
import com.soupulsar.domain.model.auth.PasswordResetToken;
import com.soupulsar.domain.model.user.User;
import com.soupulsar.domain.repository.PasswordResetTokenRepository;
import com.soupulsar.domain.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class ResetPasswordUseCaseTest {

    private PasswordEncoder passwordEncoder;
    private UserRepository userRepository;
    private PasswordResetTokenRepository passwordResetTokenRepository;
    private ResetPasswordUseCase useCase;

    @BeforeEach
    void setUp() {
        passwordEncoder = mock(PasswordEncoder.class);
        userRepository = mock(UserRepository.class);
        passwordResetTokenRepository = mock(PasswordResetTokenRepository.class);

        useCase = new ResetPasswordUseCase(passwordEncoder, userRepository, passwordResetTokenRepository);
    }

    @Test
    void execute_whenTokenNotFound_throwsInvalidToken() {
        when(passwordResetTokenRepository.findByToken("nope")).thenReturn(Optional.empty());
        assertThrows(InvalidPasswordResetTokenException.class, () -> useCase.execute(new ResetPasswordRequest("nope", "newpass")));
    }

    @Test
    void execute_whenTokenExpired_throwsExpired() {
        PasswordResetToken token = PasswordResetToken.create(UUID.randomUUID(), "t", Instant.now().minusSeconds(10));
        when(passwordResetTokenRepository.findByToken("t")).thenReturn(Optional.of(token));

        assertThrows(ExpiredPasswordResetTokenException.class, () -> useCase.execute(new ResetPasswordRequest("t", "newpass")));
    }

    @Test
    void execute_whenTokenAlreadyUsed_throws() {
        UUID userId = UUID.randomUUID();
        PasswordResetToken token = PasswordResetToken.create(userId, "usedtok", Instant.now().plusSeconds(3600));

        token.markUsed();

        when(passwordResetTokenRepository.findByToken("usedtok")).thenReturn(Optional.of(token));

        assertThrows(ExpiredPasswordResetTokenException.class, () -> useCase.execute(new ResetPasswordRequest("usedtok", "whatever")));
    }

    @Test
    void execute_happyPath_updatesPassword_and_marksTokenUsed() {
        UUID userId = UUID.randomUUID();
        PasswordResetToken token = PasswordResetToken.create(userId, "tok", Instant.now().plusSeconds(3600));
        User user = User.restore(userId, "name", "00000000000", "0000-0000", "e@x.com", "oldhash", com.soupulsar.domain.model.enums.UserRole.CLIENT, com.soupulsar.domain.model.enums.UserStatus.ACTIVE, null);

        when(passwordResetTokenRepository.findByToken("tok")).thenReturn(Optional.of(token));
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(passwordEncoder.encode("newpass")).thenReturn("newhash");

        useCase.execute(new ResetPasswordRequest("tok", "newpass"));

        assertEquals("newhash", user.getPasswordHash());
        assertTrue(token.isUsed());

        verify(userRepository, times(1)).save(user);
        verify(passwordResetTokenRepository, times(1)).save(token);
    }
}