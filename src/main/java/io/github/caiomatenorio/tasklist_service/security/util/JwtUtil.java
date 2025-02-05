package io.github.caiomatenorio.tasklist_service.security.util;

import java.time.Instant;
import java.util.Date;
import java.util.UUID;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import io.github.caiomatenorio.tasklist_service.convention.ConventionalCookie;
import io.github.caiomatenorio.tasklist_service.util.CookieUtil;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtUtil {
    @Value("${app.jwt.expirationSeconds}")
    private int jwtExpirationSeconds;

    @Value("${app.jwt.secret}")
    private String jwtSecret;

    private final CookieUtil cookieUtil;

    private SecretKey key;

    @PostConstruct
    private void init() {
        if (jwtSecret.length() < 32)
            throw new IllegalArgumentException("JWT secret must be at least 32 characters long");

        byte[] jwtSecretBytes = jwtSecret.getBytes();
        key = Keys.hmacShaKeyFor(jwtSecretBytes);
    }

    public String generateJwt(UUID sessionId, UUID userId, String username, String name) {

        Instant now = Instant.now();
        Instant expiresAt = now.plusSeconds(jwtExpirationSeconds);

        return Jwts.builder()
                .subject(sessionId.toString())
                .claim("userId", userId.toString())
                .claim("username", username)
                .claim("name", name)
                .issuedAt(Date.from(now))
                .expiration(Date.from(expiresAt))
                .signWith(key)
                .compact();
    }

    public ConventionalCookie createAuthCookie(String jwt) {
        return cookieUtil.createSecureCookie("auth_token", jwt, jwtExpirationSeconds);
    }

    public ConventionalCookie deleteAuthCookie() {
        return cookieUtil.deleteCookie("auth_token");
    }

    public boolean isTokenValid(@Nullable String token) {
        if (token == null)
            return false;

        try {
            Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    public UUID extractSessionId(String token) {
        return UUID.fromString(
                Jwts.parser()
                        .verifyWith(key)
                        .build()
                        .parseSignedClaims(token)
                        .getPayload()
                        .getSubject());
    }

    public UUID extractUserId(String token) {
        return UUID.fromString(
                Jwts.parser()
                        .verifyWith(key)
                        .build()
                        .parseSignedClaims(token)
                        .getPayload()
                        .get("userId", String.class));
    }

    public String extractUsername(String token) {
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .get("username", String.class);
    }

    public String extractName(String token) {
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .get("name", String.class);
    }
}
