package com.soupulsar.application.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record AuthUserRequest(

        @Email String email,
        @NotBlank String password

) {
}
