package com.soupulsar.infrastructure.security;

import com.soupulsar.application.interfaces.TokenHasher;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.HexFormat;

@Component
public class HmacSha256TokenHasher implements TokenHasher {

    private static final String ALGORITHM = "HmacSHA256";
    private final SecretKeySpec secretKey;

    public HmacSha256TokenHasher(@Value("${spring.security.token.hash-secret}") String secret) {
        if (secret == null || secret.isEmpty()) {
            throw new IllegalArgumentException("Secret key must not be null or empty");
        }
        this.secretKey = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), ALGORITHM);
    }

    @Override
    public String hash(String token) {

        if (token == null || token.isEmpty()) {
            throw new IllegalArgumentException("Token must not be null or empty");
        }

        try {
            Mac mac = Mac.getInstance(ALGORITHM);
            mac.init(secretKey);

            byte[] hashBytes = mac.doFinal(token.getBytes(StandardCharsets.UTF_8));

            return HexFormat.of().formatHex(hashBytes);

        } catch (Exception e) {
            throw new RuntimeException("Error while hashing token", e);
        }
    }
}