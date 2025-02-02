package io.github.caiomatenorio.tasklist_service.util;

import java.security.SecureRandom;
import java.util.Base64;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.Cookie;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class RefreshTokenUtil {
    @Value("${app.session.refreshTokenLength}")
    private int refreshTokenLength;

    @Value("${app.session.expirationSeconds}")
    private int sessionDurationSeconds;

    private static final SecureRandom SECURE_RANDOM = new SecureRandom();

    private final CookieUtil cookieUtil;

    public String generateRefreshToken() {
        byte[] randomBytes = new byte[refreshTokenLength];
        SECURE_RANDOM.nextBytes(randomBytes);

        return Base64.getUrlEncoder()
                .withoutPadding()
                .encodeToString(randomBytes);
    }

    public Cookie createRefreshCookie(String refreshToken) {
        return cookieUtil.createSecureCookie("refresh_token", refreshToken, sessionDurationSeconds);
    }
}
