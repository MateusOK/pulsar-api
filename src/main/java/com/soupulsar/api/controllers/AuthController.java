package com.soupulsar.api.controllers;

import com.soupulsar.application.dto.request.AuthUserRequest;
import com.soupulsar.application.dto.response.AuthUserResponse;
import com.soupulsar.application.dto.request.RegistrationRequest;
import com.soupulsar.application.dto.response.RegistrationResponse;
import com.soupulsar.application.usecase.AuthenticateUserUseCase;
import com.soupulsar.application.usecase.RegistrationUseCase;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticateUserUseCase authenticateUserUseCase;
    private final RegistrationUseCase registrationUseCase;


    @PostMapping(value = {"/login", "/signin"})
    public ResponseEntity<AuthUserResponse> login(@RequestBody @Valid AuthUserRequest request) {
        return ResponseEntity.ok(authenticateUserUseCase.execute(request));
    }

    @PostMapping(value = {"/register", "/signup"})
    public ResponseEntity<RegistrationResponse> register(@RequestBody @Valid RegistrationRequest request) {
        var response = registrationUseCase.execute(request);
        return ResponseEntity.created(URI.create("/api/users/" + response.userId())).body(response);
    }
}