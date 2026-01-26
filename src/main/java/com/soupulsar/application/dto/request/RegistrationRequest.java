package com.soupulsar.application.dto.request;

import com.soupulsar.domain.model.enums.SpecialistType;
import com.soupulsar.domain.model.enums.UserRole;
import com.soupulsar.domain.model.vo.*;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.br.CPF;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Schema(description = "Request DTO for user registration")
public record RegistrationRequest(

        // User info
        @Schema(description = "User's full name", example = "John Doe", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank
        String name,

        @Schema(description = "User's CPF number", example = "123.456.789-00", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank
        @CPF
        String cpf,

        @Schema(description = "User's telephone number", example = "+55 11 91234-5678", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank
        String telephone,

        @Schema(description = "User's email address", example = "email@example.com", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank
        String email,

        @Schema(description = "User's password", example = "strongPassword123", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank
        String password,

        @Schema(description = "User's role", example = "CLIENT", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotNull
        UserRole role,

        @Schema(description = "User's address", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotNull
        Address address,

        // Client info
        @Schema(description = "User's date of birth", example = "1990-01-01")
        Date dateOfBirth,
        @Schema(description = "User's emergency contact information")
        EmergencyContact emergencyContact,

        // Specialist info
        @Schema(description = "Type of specialist", example = "PSICOLOGO")
        SpecialistType specialistType,
        @Schema(description = "Price per session", example = "150.00")
        Money sessionPrice,
        @Schema(description = "Professional registration number", example = "CRP 06/12345")
        RegistrationNumber registrationNumber,
        @Schema(description = "Specialist presentation details")
        Presentation presentation
) {
}