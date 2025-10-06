package com.soupulsar.modulith.auth.application.usecase;

import com.soupulsar.modulith.auth.application.dto.AuthUserRequest;
import com.soupulsar.modulith.auth.application.security.JwtService;
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
    private JwtService jwtService;

    @BeforeEach
    void setUp() {
        userRepository = mock(UserRepository.class);
        passwordHasher = mock(PasswordHasher.class);
        jwtService = mock(JwtService.class);
        useCase = new AuthenticateUserUseCase(userRepository, passwordHasher, jwtService);
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
        when(jwtService.generateToken(user)).thenReturn("JWT-TOKEN");

        String token = useCase.execute(request);

        assertEquals("JWT-TOKEN", token);
        verify(jwtService).generateToken(user);
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
        verify(jwtService, never()).generateToken(user);
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
        verify(jwtService, never()).generateToken(user);
    }

    @Test
    void shouldThrowWhenUserNotFound() {
        AuthUserRequest request = new AuthUserRequest("notfound@email.com", "password");

        when(userRepository.findByEmail(request.email())).thenReturn(Optional.empty());

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> useCase.execute(request));
        assertEquals("Invalid email or password", ex.getMessage());
        verify(jwtService, never()).generateToken(any());
    }
}