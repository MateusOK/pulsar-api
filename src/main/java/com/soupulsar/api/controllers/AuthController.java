package com.soupulsar.api.controllers;

import com.soupulsar.application.dto.request.AuthUserRequest;
import com.soupulsar.application.dto.request.ResetPasswordEmailRequest;
import com.soupulsar.application.dto.request.ResetPasswordRequest;
import com.soupulsar.application.dto.response.AuthUserResponse;
import com.soupulsar.application.dto.request.RegistrationRequest;
import com.soupulsar.application.dto.response.RegistrationResponse;
import com.soupulsar.application.usecase.auth.AuthenticateUserUseCase;
import com.soupulsar.application.usecase.auth.RegistrationUseCase;
import com.soupulsar.application.usecase.auth.RequestPasswordResetUseCase;
import com.soupulsar.application.usecase.auth.ResetPasswordUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Authentication", description = "Endpoints for user authentication and registration")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticateUserUseCase authenticateUserUseCase;
    private final RegistrationUseCase registrationUseCase;
    private final RequestPasswordResetUseCase  requestPasswordResetUseCase;
    private final ResetPasswordUseCase resetPasswordUseCase;

    @Operation(summary = "User Login", description = "Authenticate a user and return an authentication token")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User authenticated successfully", content = {@Content(schema = @Schema(implementation = AuthUserResponse.class))}),
            @ApiResponse(responseCode = "401", description = "Invalid credentials")
    })
    @PostMapping(value = {"/login", "/signin"})
    public ResponseEntity<AuthUserResponse> login(@RequestBody @Valid AuthUserRequest request) {
        return ResponseEntity.ok(authenticateUserUseCase.execute(request));
    }

    @Operation(summary = "User Registration", description = "Register a new user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User registered successfully", content = {@Content(schema = @Schema(implementation = RegistrationResponse.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid registration data")
    })
    @PostMapping(value = {"/register", "/signup"})
    public ResponseEntity<RegistrationResponse> register(@RequestBody @Valid RegistrationRequest request) {
        var response = registrationUseCase.execute(request);
        return ResponseEntity.created(URI.create("/api/users/" + response.userId())).body(response);
    }

    @PostMapping(value = "/reset-password/request")
    public ResponseEntity<String> resetPasswordRequest(@RequestBody ResetPasswordEmailRequest request) {
        String message = "If an account with that email exists, a password reset link has been sent.";
        requestPasswordResetUseCase.execute(request);
        return ResponseEntity.ok(message);
    }

    @PostMapping(value = "/reset-password/confirm")
    public ResponseEntity<String> resetPasswordConfirm(@RequestBody ResetPasswordRequest request) {
        resetPasswordUseCase.execute(request);
        return ResponseEntity.ok("Password has been reset successfully.");
    }

}