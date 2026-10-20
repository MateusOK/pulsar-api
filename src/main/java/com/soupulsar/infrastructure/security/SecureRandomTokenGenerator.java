package com.soupulsar.infrastructure.security;

import com.soupulsar.application.interfaces.TokenGenerator;
import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.util.Base64;

@Component
public class SecureRandomTokenGenerator implements TokenGenerator {

    private static final String PARTNER_CODE_CHARACTERS = "ABCDEFGHJKLMNPQRSTUVWXYZ23456789";
    private static final int PARTNER_CODE_LENGTH = 8;

    private final SecureRandom random = new SecureRandom();

    @Override
    public String generate() {
        byte[] bytes = new byte[32];
        random.nextBytes(bytes);
        return Base64.getEncoder()
                .withoutPadding()
                .encodeToString(bytes);
    }

    @Override
    public String generatePartnerCode() {
        StringBuilder code = new StringBuilder(PARTNER_CODE_LENGTH);

        for (int i = 0; i < PARTNER_CODE_LENGTH; i++) {
            int index = random.nextInt(PARTNER_CODE_CHARACTERS.length());
            code.append(PARTNER_CODE_CHARACTERS.charAt(index));
        }
        return code.toString();
    }
}