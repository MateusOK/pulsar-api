package com.soupulsar.application.dto.request;

public record ResetPasswordRequest(String token, String newPassword) {
}
