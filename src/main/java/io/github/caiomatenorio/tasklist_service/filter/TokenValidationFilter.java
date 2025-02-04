package io.github.caiomatenorio.tasklist_service.filter;

import java.io.IOException;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.github.caiomatenorio.tasklist_service.convention.ConventionalCookie;
import io.github.caiomatenorio.tasklist_service.convention.ConventionalResponseBody;
import io.github.caiomatenorio.tasklist_service.dto.RefreshSessionOutput;
import io.github.caiomatenorio.tasklist_service.exception.RefreshTokenException;
import io.github.caiomatenorio.tasklist_service.service.SessionService;
import io.github.caiomatenorio.tasklist_service.util.CookieUtil;
import io.github.caiomatenorio.tasklist_service.util.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class TokenValidationFilter extends OncePerRequestFilter {
    private final JwtUtil jwtUtil;
    private final SessionService sessionService;
    private final ObjectMapper objectMapper;
    private final CookieUtil cookieUtil;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain) throws ServletException, IOException {
        Map<String, String> cookieMap = cookieUtil.toMap(request, "auth_token", "refresh_token");
        String jwt = cookieMap.get("auth_token");

        if (jwtUtil.isTokenValid(jwt)) {
            UUID sessionId = jwtUtil.extractSessionId(jwt);
            String username = jwtUtil.extractUsername(jwt);
            authenticate(username, sessionId);

            filterChain.doFilter(request, response);
            return;
        }

        try {
            // If the JWT is invalid, try to refresh the session
            String refreshToken = cookieMap.get("refresh_token");
            RefreshSessionOutput output = sessionService.refreshSession(refreshToken);
            authenticate(output.username(), output.id());

            Set<Cookie> newCookies = sessionService.createSessionCookies(output.id()).stream()
                    .map(ConventionalCookie::toServletCookie)
                    .collect(Collectors.toSet());

            newCookies.forEach(response::addCookie);
            filterChain.doFilter(request, response);
        } catch (RefreshTokenException e) {
            // If the refresh token is invalid, delete the cookies and send an error
            // response
            cookieUtil.deleteCookies("auth_token", "refresh_token").stream()
                    .map(ConventionalCookie::toServletCookie)
                    .forEach(response::addCookie);

            sendErrorResponse(response, 401, e.getMessage());
        } catch (NoSuchElementException e) {
            sendErrorResponse(response, 500, "An error occurred while processing the request");
        }
    }

    private void authenticate(String username, UUID sessionId) {
        try {
            SecurityContextHolder.getContext()
                    .setAuthentication(UsernamePasswordAuthenticationToken.authenticated(username, sessionId, null));
        } catch (NoSuchElementException e) {
            throw new IllegalArgumentException("Session not found");
        }
    }

    private void sendErrorResponse(HttpServletResponse response, int status, String message)
            throws IOException {
        ConventionalResponseBody responseBody = new ConventionalResponseBody.Error(status, message);

        response.setStatus(status);
        response.setContentType("application/json");
        objectMapper.writeValue(response.getWriter(), responseBody);
    }
}
