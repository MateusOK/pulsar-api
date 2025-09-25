package com.soupulsar.modulith.auth.api.controllers;

import com.soupulsar.modulith.auth.application.dto.AuthUserRequest;
import com.soupulsar.modulith.auth.application.dto.CreateUserRequest;
import com.soupulsar.modulith.auth.application.dto.CreateUserResponse;
import com.soupulsar.modulith.auth.application.usecase.AuthenticateUserUseCase;
import com.soupulsar.modulith.auth.application.usecase.RegisterUserUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticateUserUseCase authenticateUserUseCase;
    private final RegisterUserUseCase registerUserUseCase;


    @PostMapping(value = {"/login", "/signin"})
    public ResponseEntity<String> login(@RequestBody AuthUserRequest request) {
        String token = authenticateUserUseCase.execute(request);
        return ResponseEntity.ok(token);
    }

    @PostMapping(value = {"/register", "/signup"})
    public ResponseEntity<CreateUserResponse> register(@RequestBody CreateUserRequest request) {
        var response = registerUserUseCase.execute(request);
        return ResponseEntity.ok(response);

    }
}