package com.soupulsar.application.dto.response;

import com.soupulsar.domain.model.vo.EmergencyContact;
import lombok.Builder;

import java.util.Date;

@Builder
public record ClientProfileResponse(
        String name,
        String email,
        String telephone,
        Date birthday,
        EmergencyContact emergencyContact,
        AddressResponse addressResponse
) implements UserProfileResponse {
}
