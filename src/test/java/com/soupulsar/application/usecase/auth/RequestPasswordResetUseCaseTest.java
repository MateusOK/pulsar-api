package com.soupulsar.application.usecase.auth;

import com.soupulsar.application.dto.request.ResetPasswordEmailRequest;
import com.soupulsar.application.interfaces.EmailGateway;
import com.soupulsar.application.interfaces.TokenGenerator;
import com.soupulsar.domain.exceptions.UserNotFoundException;
import com.soupulsar.domain.model.auth.PasswordResetToken;
import com.soupulsar.domain.model.user.User;
import com.soupulsar.domain.model.enums.UserRole;
import com.soupulsar.domain.model.enums.UserStatus;
import com.soupulsar.domain.repository.PasswordResetTokenRepository;
import com.soupulsar.domain.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.contains;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class RequestPasswordResetUseCaseTest {

    private UserRepository userRepository;
    private TokenGenerator tokenGenerator;
    private PasswordResetTokenRepository passwordResetTokenRepository;
    private EmailGateway emailGateway;
    private RequestPasswordResetUseCase useCase;

    @BeforeEach
    void setUp() {
        userRepository = mock(UserRepository.class);
        tokenGenerator = mock(TokenGenerator.class);
        passwordResetTokenRepository = mock(PasswordResetTokenRepository.class);
        emailGateway = mock(EmailGateway.class);
        useCase = new RequestPasswordResetUseCase(userRepository, tokenGenerator, passwordResetTokenRepository, emailGateway, "http://frontend.app");
    }

    @Test
    void execute_whenUserNotFound_throws() {
        when(userRepository.findByEmail("notfound@example.com")).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> useCase.execute(new ResetPasswordEmailRequest("notfound@example.com")));
    }

    @Test
    void execute_happyPath_generatesToken_savesAndSendsEmail() {
        UUID userId = UUID.randomUUID();
        User user = User.restore(userId, "name", "00000000000", "0000-0000", "test@example.com", "hash", UserRole.CLIENT, UserStatus.ACTIVE, null);

        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(user));
        when(tokenGenerator.generate()).thenReturn("token123");

        useCase.execute(new ResetPasswordEmailRequest("test@example.com"));

        ArgumentCaptor<PasswordResetToken> captor = ArgumentCaptor.forClass(PasswordResetToken.class);
        verify(passwordResetTokenRepository, times(1)).save(captor.capture());

        PasswordResetToken saved = captor.getValue();
        assertEquals(userId, saved.getUserId());
        assertEquals("token123", saved.getToken());

        java.time.Duration duration = java.time.Duration.between(Instant.now(), saved.getExpiresAt());
        assertTrue(duration.toMinutes() >= 59, "Expiration should be approximately 1 hour");

        verify(emailGateway, times(1)).sendPasswordResetEmail(contains("token123"), eq("name"), eq("test@example.com"));
    }

}