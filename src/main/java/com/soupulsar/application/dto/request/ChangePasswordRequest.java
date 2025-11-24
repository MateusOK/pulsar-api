package com.soupulsar.application.dto.request;

public record ChangePasswordRequest(
        String currentPassword,
        String newPassword
) {
}
