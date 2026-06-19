package com.soupulsar.infrastructure.security;

import com.soupulsar.application.interfaces.TokenGenerator;
import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.util.Base64;

@Component
public class SecureRandomTokenGenerator implements TokenGenerator {

    private final SecureRandom random = new SecureRandom();

    @Override
    public String generate() {
        byte[] bytes = new byte[32];
        random.nextBytes(bytes);
        return Base64.getEncoder()
                .withoutPadding()
                .encodeToString(bytes);
    }

}