package com.soupulsar.application.dto.request;

import com.soupulsar.domain.model.vo.Address;
import com.soupulsar.domain.model.vo.EmergencyContact;
import jakarta.validation.constraints.Email;

public record UpdateUserProfileRequest(

        String name,
        String phone,
        Address address,
        EmergencyContact emergencyContact,
        @Email
        String email
) {
}