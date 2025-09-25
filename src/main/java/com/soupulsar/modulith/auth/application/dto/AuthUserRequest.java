package com.soupulsar.modulith.auth.application.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record AuthUserRequest(

        @Email String email,
        @NotBlank String password

) {
}
