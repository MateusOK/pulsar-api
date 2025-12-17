package com.soupulsar.application.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Request DTO for user authentication")
public record AuthUserRequest(
        @Schema(description = "User's email address", example = "email@example.com", requiredMode = Schema.RequiredMode.REQUIRED)
        @Email
        String email,
        @Schema(description = "User's password", example = "strongPassword123", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank
        String password

) {
}
