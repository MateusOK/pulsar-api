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
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/users")
@Tag(name = "Users", description = "Endpoints for managing users")
@RequiredArgsConstructor
public class UserController {

    private final GetAllUsersUseCase getAllUsersUseCase;
    private final GetUserByIdUseCase getUserByIdUseCase;
    private final GetUserProfileUseCase getUserProfileUseCase;
    private final UpdateUserProfileUseCase updateUserProfileUseCase;
    private final ChangePasswordUseCase changePasswordUseCase;

    @Operation(summary = "Get User by ID", description = "Retrieve a user by their unique ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User found successfully"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @GetMapping("/{userId}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable UUID userId) {
        var response = getUserByIdUseCase.execute(userId);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Get All Users", description = "Retrieve a paginated list of users with optional filtering by status and role")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Users retrieved successfully")
    })
    @GetMapping
    public ResponseEntity<Page<User>> getAllUsers(
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size,
            @RequestParam(required = false) UserStatus status,
            @RequestParam(required = false) UserRole role
    ) {

        return ResponseEntity.ok(getAllUsersUseCase.execute(GetAllUsersRequest.of(page, size, status, role)));
    }

    @Operation(summary = "Get User Profile", description = "Retrieve the profile of the currently authenticated user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Profile retrieved successfully", content = {@Content(schema = @Schema(implementation = UserProfileResponse.class))}),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @GetMapping("/me")
    public ResponseEntity<UserProfileResponse> getProfile() {
        var response = getUserProfileUseCase.execute();
        return ResponseEntity.ok(response);

    }

    @Operation(summary = "Update User Profile", description = "Update the profile of the currently authenticated user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Profile updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @PatchMapping("/me")
    public ResponseEntity<Void> updateProfile(@RequestBody UpdateUserProfileRequest request) {
        updateUserProfileUseCase.execute(request);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Change Password", description = "Change the password of the currently authenticated user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Password changed successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @PutMapping("/me/password")
    public ResponseEntity<Void> updatePassword(@RequestBody ChangePasswordRequest request) {
        changePasswordUseCase.execute(request);
        return ResponseEntity.ok().build();
    }
}