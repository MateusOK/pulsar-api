package com.soupulsar.application.dto.response;

import com.soupulsar.domain.model.vo.EmergencyContact;
import lombok.Builder;
import org.springframework.cglib.core.Local;

import java.time.LocalDate;
import java.util.Date;

@Builder
public record ClientProfileResponse(
        String name,
        String email,
        String telephone,
        LocalDate birthday,
        EmergencyContact emergencyContact,
        AddressResponse addressResponse
) implements UserProfileResponse {
}
