package io.github.caiomatenorio.tasklist_service.util;

import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

import io.github.caiomatenorio.tasklist_service.convention.ConventionalCookie;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;

@Component
public class CookieUtil {
    @Value("${spring.profiles.active}")
    private String activeProfile;

    public ConventionalCookie createSecureCookie(String name, String value, int maxAgeSeconds) {
        boolean secure = "prod".equals(activeProfile);

        return new ConventionalCookie(
                name,
                value,
                maxAgeSeconds,
                secure,
                true,
                "/",
                "Strict");
    }

    public Optional<String> getCookieValue(HttpServletRequest request, String name) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null)
            return Optional.empty();

        return Arrays.stream(cookies)
                .filter(cookie -> cookie.getName().equals(name))
                .map(Cookie::getValue)
                .findFirst();
    }

    public Map<String, String> toMap(HttpServletRequest request, String... selectedCookies) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null)
            return Map.of();

        Set<String> selectedCookieSet = Set.of(selectedCookies);

        return Arrays.stream(cookies)
                .filter(cookie -> selectedCookieSet.contains(cookie.getName()))
                .collect(Collectors.toMap(Cookie::getName, Cookie::getValue));
    }

    public HttpHeaders toHttpHeaders(Set<ConventionalCookie> cookies) {
        HttpHeaders headers = new HttpHeaders();
        cookies.stream()
                .map(ConventionalCookie::toResponseCookie)
                .forEach(cookie -> headers.add(HttpHeaders.SET_COOKIE, cookie.toString()));

        return headers;
    }

    public ConventionalCookie deleteCookie(String name) {
        return new ConventionalCookie(
                name,
                "",
                0,
                true,
                true,
                "/",
                "Strict");
    }

    public Set<ConventionalCookie> deleteCookies(String... names) {
        return Arrays.stream(names)
                .map(this::deleteCookie)
                .collect(Collectors.toSet());
    }
}
