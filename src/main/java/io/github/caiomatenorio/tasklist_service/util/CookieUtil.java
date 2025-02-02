package io.github.caiomatenorio.tasklist_service.util;

import java.util.Arrays;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;

@Component
public class CookieUtil {
    @Value("${spring.profiles.active}")
    private String activeProfile;

    public Cookie createSecureCookie(String name, String value, int maxAgeSeconds) {
        Cookie cookie = new Cookie(name, value);

        if ("prod".equals(activeProfile))
            cookie.setSecure(true);

        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setAttribute("SameSite", "Strict");
        cookie.setMaxAge(maxAgeSeconds);

        return cookie;
    }

    public Map<String, String> extractCookies(HttpServletRequest request, String... names) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null)
            return Map.of();

        Set<String> nameSet = Set.of(names);

        return Arrays.stream(cookies).filter(cookie -> nameSet.contains(cookie.getName()))
                .collect(Collectors.toMap(Cookie::getName, Cookie::getValue));
    }
}
