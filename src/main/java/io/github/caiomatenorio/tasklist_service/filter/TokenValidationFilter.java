package io.github.caiomatenorio.tasklist_service.filter;

import java.io.IOException;
import java.time.Instant;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;

import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.github.caiomatenorio.tasklist_service.dto.ResponseBody;
import io.github.caiomatenorio.tasklist_service.dto.output.RefreshSessionOutput;
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
        Map<String, String> cookieMap = cookieUtil.extractCookies(request, "auth_token", "refresh_token");
        String jwt = cookieMap.get("auth_token");

        if (jwtUtil.isTokenValid(jwt)) {
            String username = jwtUtil.extractUsername(jwt);
            authenticate(username);

            filterChain.doFilter(request, response);
            return;
        }

        try {
            String refreshToken = cookieMap.get("refresh_token");
            RefreshSessionOutput output = sessionService.refreshSession(refreshToken);
            authenticate(output.username());

            Set<Cookie> newCookies = sessionService.createSessionCookies(output.id());
            newCookies.forEach(response::addCookie);

            filterChain.doFilter(request, response);
        } catch (RefreshTokenException e) {
            sendErrorResponse(
                    response,
                    HttpStatus.UNAUTHORIZED,
                    e.getMessage());
        } catch (NoSuchElementException e) {
            sendErrorResponse(
                    response,
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "An error occurred while processing the request");
        }
    }

    private void authenticate(String username) {
        SecurityContextHolder.getContext()
                .setAuthentication(UsernamePasswordAuthenticationToken.authenticated(username, null, null));
    }

    private void sendErrorResponse(HttpServletResponse response, HttpStatus status, String message)
            throws IOException {
        ResponseBody responseBody = new ResponseBody.Error(
                status,
                Instant.now(),
                message);

        response.setStatus(status.value());
        response.setContentType("application/json");

        objectMapper.writeValue(response.getWriter(), responseBody);
    }
}
