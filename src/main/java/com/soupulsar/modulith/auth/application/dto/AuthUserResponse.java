package com.soupulsar.modulith.auth.application.dto;

import java.util.Date;

public record AuthUserResponse(

        String accessToken,
        String tokenType,
        String subject,
        Date issuedAt,
        Long expiresIn
) {
}