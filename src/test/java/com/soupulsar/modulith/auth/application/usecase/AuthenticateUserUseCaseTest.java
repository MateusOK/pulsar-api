package com.soupulsar.modulith.auth.application.usecase;

import com.soupulsar.modulith.auth.application.dto.AuthUserRequest;
import com.soupulsar.modulith.auth.application.dto.AuthUserResponse;
import com.soupulsar.modulith.auth.application.security.JwtService;
import com.soupulsar.modulith.auth.application.security.PasswordHasher;
import com.soupulsar.modulith.auth.domain.model.User;
import com.soupulsar.modulith.auth.domain.model.enums.UserRole;
import com.soupulsar.modulith.auth.domain.model.enums.UserStatus;
import com.soupulsar.modulith.auth.domain.model.vo.Address;
import com.soupulsar.modulith.auth.domain.repository.UserRepository;
import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class AuthenticateUserUseCaseTest {

    private UserRepository userRepository;
    private PasswordHasher passwordHasher;
    private AuthenticateUserUseCase useCase;
    private JwtService jwtService;
    private Address address;

    @BeforeEach
    void setUp() {
        userRepository = mock(UserRepository.class);
        passwordHasher = mock(PasswordHasher.class);
        jwtService = mock(JwtService.class);
        address = mock(Address.class);
        useCase = new AuthenticateUserUseCase(userRepository, passwordHasher, jwtService);
    }

    @Test
    void shouldAuthenticateActiveUserWithCorrectPassword() {
        AuthUserRequest request = new AuthUserRequest("user@email.com", "password");
        User user = User.restore(
                UUID.randomUUID(), "Test User", "12345678900", "999999999", "user@email.com",
                "hashedPassword", UserRole.CLIENT, UserStatus.ACTIVE, address
        );

        when(userRepository.findByEmail(request.email())).thenReturn(Optional.of(user));
        when(passwordHasher.matches(request.password(), user.getPasswordHash())).thenReturn(true);
        when(jwtService.generateToken(user)).thenReturn("JWT-TOKEN");

        Date now = new Date();
        Date expiration = new Date(now.getTime() + 3600_000);

        when(jwtService.extractClaim(eq("JWT-TOKEN"), any())).thenAnswer(invocation -> {
            var resolver = invocation.<java.util.function.Function<Claims, ?>>getArgument(1);

            Claims fakeClaims = mock(Claims.class);
            when(fakeClaims.getIssuedAt()).thenReturn(now);
            when(fakeClaims.getSubject()).thenReturn(user.getEmail());
            when(fakeClaims.getExpiration()).thenReturn(expiration);

            return resolver.apply(fakeClaims);
        });


        AuthUserResponse token = useCase.execute(request);

        assertEquals("JWT-TOKEN", token.accessToken());
        assertEquals("Bearer", token.tokenType());
        assertEquals(user.getEmail(), token.subject());
        assertEquals(expiration.getTime() - now.getTime(), token.expiresIn() * 1000, 5000);
        verify(jwtService).generateToken(user);
    }

    @Test
    void shouldThrowWhenUserIsInactive() {
        AuthUserRequest request = new AuthUserRequest("user@email.com", "password");
        User user = User.restore(
                UUID.randomUUID(), "Test User", "12345678900", "999999999", "user@email.com",
                "hashedPassword", UserRole.CLIENT, UserStatus.INACTIVE, address
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
                "hashedPassword", UserRole.CLIENT, UserStatus.ACTIVE, address
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