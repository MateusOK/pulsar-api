package com.soupulsar.modulith.auth.application.dto;

import com.soupulsar.modulith.auth.domain.model.enums.UserRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.br.CPF;

public record CreateUserRequest(

        @NotBlank
        String name,
        @Email
        String email,
        @CPF
        String cpf,
        @NotBlank
        String telephone,
        @NotBlank
        String password,
        @NotBlank
        UserRole role

) {
}