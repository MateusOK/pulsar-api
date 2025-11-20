package com.soupulsar.api.controllers;

import com.soupulsar.application.dto.request.ChangePasswordRequest;
import com.soupulsar.application.dto.request.GetAllUsersRequest;
import com.soupulsar.application.dto.request.UpdateUserProfileRequest;
import com.soupulsar.application.dto.response.UserProfileResponse;
import com.soupulsar.application.dto.response.UserResponse;
import com.soupulsar.application.usecase.*;
import com.soupulsar.domain.model.enums.UserRole;
import com.soupulsar.domain.model.enums.UserStatus;
import com.soupulsar.domain.model.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final GetAllUsersUseCase getAllUsersUseCase;
    private final GetUserByIdUseCase getUserByIdUseCase;
    private final GetUserProfileUseCase getUserProfileUseCase;
    private final UpdateUserProfileUseCase updateUserProfileUseCase;
    private final ChangePasswordUseCase changePasswordUseCase;

    @GetMapping("/{userId}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable UUID userId) {
        var response = getUserByIdUseCase.execute(userId);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<Page<User>> getAllUsers(
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size,
            @RequestParam(required = false) UserStatus status,
            @RequestParam(required = false) UserRole role
    ) {

        return ResponseEntity.ok(getAllUsersUseCase.execute(GetAllUsersRequest.of(page, size, status, role)));
    }

    @GetMapping("/me")
    public ResponseEntity<UserProfileResponse> getProfile() {
        var response = getUserProfileUseCase.execute();
        return ResponseEntity.ok(response);

    }

    @PatchMapping("/me")
    public ResponseEntity<Void> updateProfile(@RequestBody UpdateUserProfileRequest request) {
        updateUserProfileUseCase.execute(request);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/me/password")
    public ResponseEntity<Void> updatePassword(@RequestBody ChangePasswordRequest request) {
        changePasswordUseCase.execute(request);
        return ResponseEntity.ok().build();
    }
}