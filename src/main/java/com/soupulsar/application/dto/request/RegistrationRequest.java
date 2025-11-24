package com.soupulsar.application.dto.request;

import com.soupulsar.domain.model.enums.SpecialistType;
import com.soupulsar.domain.model.enums.UserRole;
import com.soupulsar.domain.model.vo.Address;
import com.soupulsar.domain.model.vo.EmergencyContact;
import com.soupulsar.domain.model.vo.Presentation;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.br.CPF;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public record RegistrationRequest(

        // User info
        @NotBlank
        String name,
        @NotBlank
        @CPF
        String cpf,
        @NotBlank
        String telephone,
        @NotBlank
        String email,
        @NotBlank
        String password,
        @NotNull
        UserRole role,
        @NotNull
        Address address,

        // Client info
        Date dateOfBirth,
        EmergencyContact emergencyContact,

        // Specialist info
        SpecialistType specialistType,
        BigDecimal sessionPrice,
        String registrationNumber,
        Presentation presentation,
        List<String> formations,
        List<String> specialties,
        List<String> approaches
) {
}