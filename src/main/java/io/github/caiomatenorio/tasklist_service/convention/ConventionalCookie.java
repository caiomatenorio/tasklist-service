package io.github.caiomatenorio.tasklist_service.convention;

import org.springframework.http.ResponseCookie;

import jakarta.servlet.http.Cookie;

public record ConventionalCookie(
        String name,
        String value,
        int maxAgeSeconds,
        boolean secure,
        boolean httpOnly,
        String path,
        String sameSite) {
    public ConventionalCookie {
        if (maxAgeSeconds < 0)
            throw new IllegalArgumentException("Max age must be non-negative");
    }

    public Cookie toServletCookie() {
        Cookie cookie = new Cookie(name, value);
        cookie.setMaxAge(maxAgeSeconds);
        cookie.setSecure(secure);
        cookie.setHttpOnly(httpOnly);
        cookie.setPath(path);
        cookie.setAttribute("SameSite", sameSite);

        return cookie;
    }

    public ResponseCookie toResponseCookie() {
        return ResponseCookie.from(name, value)
                .maxAge(maxAgeSeconds)
                .secure(secure)
                .httpOnly(httpOnly)
                .path(path)
                .sameSite(sameSite)
                .build();
    }
}