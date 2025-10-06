package com.soupulsar.modulith.auth.application.security;

import com.soupulsar.modulith.auth.domain.model.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.function.Function;

@Service
public class JwtService {

    private final String secret;
    private SecretKey secretKey;
    private final Long expirationMs;

    public JwtService(@Value("${spring.security.jwt.secret}") String secret,
                      @Value("${spring.security.jwt.expiration}") Long expirationMs) {
        this.secret = secret;
        this.expirationMs = expirationMs;
    }

    @PostConstruct
    public void init() {
        if (secret == null || secret.isBlank()) {
            throw new IllegalArgumentException("JWT Secret não configurada!");
        }
        this.secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }


    public String generateToken(User user) {

        Date issueDate = new Date();
        Date expiryDate = new Date(issueDate.getTime() + expirationMs);

        return Jwts.builder()
                .subject(user.getEmail())
                .claim("userId", user.getUserId().toString())
                .claim("role", user.getRole())
                .claim("status", user.getStatus())
                .issuer("SouPulsar-AuthService")
                .audience().add("SouPulsar-API").and()
                .issuedAt(issueDate)
                .expiration(expiryDate)
                .signWith(secretKey)
                .compact();
    }


    public boolean isTokenValid(String token, String username) {
        final String extractedUsername = extractClaim(token, Claims::getSubject);
        return (extractedUsername.equals(username) && !isTokenExpired(token));
    }


    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private boolean isTokenExpired(String token) {
        return extractClaim(token, Claims::getExpiration).before(new Date());
    }

}
