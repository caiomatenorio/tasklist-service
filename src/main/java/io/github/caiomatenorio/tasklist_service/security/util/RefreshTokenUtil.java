package io.github.caiomatenorio.tasklist_service.security.util;

import java.security.SecureRandom;
import java.util.Base64;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.github.caiomatenorio.tasklist_service.convention.ConventionalCookie;
import io.github.caiomatenorio.tasklist_service.util.CookieUtil;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class RefreshTokenUtil {
    @Value("${app.session.refreshTokenLength}")
    private int refreshTokenLength;

    @Value("${app.session.expirationSeconds}")
    private int sessionDurationSeconds;

    private final SecureRandom secureRandom;
    private final CookieUtil cookieUtil;

    public String generateRefreshToken() {
        byte[] randomBytes = new byte[refreshTokenLength];
        secureRandom.nextBytes(randomBytes);

        return Base64.getUrlEncoder()
                .withoutPadding()
                .encodeToString(randomBytes);
    }

    public ConventionalCookie createRefreshCookie(String refreshToken) {
        return cookieUtil.createSecureCookie("refresh_token", refreshToken, sessionDurationSeconds);
    }
}
