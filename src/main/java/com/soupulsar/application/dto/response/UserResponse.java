package com.soupulsar.application.dto.response;



import com.soupulsar.domain.model.enums.UserRole;
import com.soupulsar.domain.model.enums.UserStatus;
import com.soupulsar.domain.model.user.User;

import java.util.UUID;

public record UserResponse(

        UUID userId,
        String name,
        String cpf,
        String email,
        String telephone,
        UserRole role,
        UserStatus status,
        AddressResponse address
) {

    public UserResponse(User response){
        this(
                response.getUserId(),
                response.getName(),
                response.getCpf(),
                response.getEmail(),
                response.getTelephone(),
                response.getRole(),
                response.getStatus(),
                AddressResponse.toResponse(response.getAddress())
        );
    }
}