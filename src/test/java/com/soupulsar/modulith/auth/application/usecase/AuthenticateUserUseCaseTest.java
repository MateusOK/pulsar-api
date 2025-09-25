package com.soupulsar.modulith.auth.application.usecase;

import com.soupulsar.modulith.auth.application.dto.AuthUserRequest;
import com.soupulsar.modulith.auth.application.security.PasswordHasher;
import com.soupulsar.modulith.auth.domain.model.User;
import com.soupulsar.modulith.auth.domain.model.enums.UserRole;
import com.soupulsar.modulith.auth.domain.model.enums.UserStatus;
import com.soupulsar.modulith.auth.domain.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuthenticateUserUseCaseTest {

    private UserRepository userRepository;
    private PasswordHasher passwordHasher;
    private AuthenticateUserUseCase useCase;

    @BeforeEach
    void setUp() {
        userRepository = mock(UserRepository.class);
        passwordHasher = mock(PasswordHasher.class);
        useCase = new AuthenticateUserUseCase(userRepository, passwordHasher);
    }

    @Test
    void shouldAuthenticateActiveUserWithCorrectPassword() {
        AuthUserRequest request = new AuthUserRequest("user@email.com", "password");
        User user = User.restore(
                UUID.randomUUID(), "Test User", "12345678900", "999999999", "user@email.com",
                "hashedPassword", UserRole.CLIENT, UserStatus.ACTIVE
        );

        when(userRepository.findByEmail(request.email())).thenReturn(Optional.of(user));
        when(passwordHasher.matches(request.password(), user.getPasswordHash())).thenReturn(true);

        String token = useCase.execute(request);

        assertEquals("JWT-TOKEN", token);
    }

    @Test
    void shouldThrowWhenUserIsInactive() {
        AuthUserRequest request = new AuthUserRequest("user@email.com", "password");
        User user = User.restore(
                UUID.randomUUID(), "Test User", "12345678900", "999999999", "user@email.com",
                "hashedPassword", UserRole.CLIENT, UserStatus.INACTIVE
        );

        when(userRepository.findByEmail(request.email())).thenReturn(Optional.of(user));

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> useCase.execute(request));
        assertEquals("User is not active", ex.getMessage());
    }

    @Test
    void shouldThrowWhenPasswordIsInvalid() {
        AuthUserRequest request = new AuthUserRequest("user@email.com", "wrong");
        User user = User.restore(
                UUID.randomUUID(), "Test User", "12345678900", "999999999", "user@email.com",
                "hashedPassword", UserRole.CLIENT, UserStatus.ACTIVE
        );

        when(userRepository.findByEmail(request.email())).thenReturn(Optional.of(user));
        when(passwordHasher.matches(request.password(), user.getPasswordHash())).thenReturn(false);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> useCase.execute(request));
        assertEquals("Invalid email or password", ex.getMessage());
    }

    @Test
    void shouldThrowWhenUserNotFound() {
        AuthUserRequest request = new AuthUserRequest("notfound@email.com", "password");

        when(userRepository.findByEmail(request.email())).thenReturn(Optional.empty());

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> useCase.execute(request));
        assertEquals("Invalid email or password", ex.getMessage());
    }
}
