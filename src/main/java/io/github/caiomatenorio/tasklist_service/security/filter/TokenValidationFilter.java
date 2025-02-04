package io.github.caiomatenorio.tasklist_service.security.filter;

import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.springframework.lang.NonNull;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import io.github.caiomatenorio.tasklist_service.convention.ConventionalCookie;
import io.github.caiomatenorio.tasklist_service.exception.InvalidRefreshTokenException;
import io.github.caiomatenorio.tasklist_service.exception.NoRefreshTokenProvidedException;
import io.github.caiomatenorio.tasklist_service.model.Session;
import io.github.caiomatenorio.tasklist_service.security.token.AuthenticationToken;
import io.github.caiomatenorio.tasklist_service.security.util.JwtUtil;
import io.github.caiomatenorio.tasklist_service.service.SessionService;
import io.github.caiomatenorio.tasklist_service.util.CookieUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class TokenValidationFilter extends OncePerRequestFilter {
    private final JwtUtil jwtUtil;
    private final SessionService sessionService;
    private final CookieUtil cookieUtil;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain) throws ServletException, IOException {
        boolean skipped = skipIfLoginOrSignup(request, response, filterChain);

        if (skipped)
            return;

        skipped = useJwt(request, response, filterChain);

        if (skipped)
            return;

        useRefreshToken(request, response, filterChain);
    }

    private boolean skipIfLoginOrSignup(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws IOException, ServletException {
        String uri = request.getRequestURI();

        if (List.of("/login", "/signup").contains(uri)) {
            filterChain.doFilter(request, response);
            return true;
        }

        return false;
    }

    private boolean useJwt(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws IOException, ServletException {
        String jwt = cookieUtil.getCookieValue(request, "auth_token").orElse(null);

        if (jwtUtil.isTokenValid(jwt)) {
            authenticate(
                    jwtUtil.extractUserId(jwt),
                    jwtUtil.extractSessionId(jwt),
                    jwtUtil.extractUsername(jwt),
                    jwtUtil.extractName(jwt));

            filterChain.doFilter(request, response);
            return true;
        }

        return false;
    }

    private void useRefreshToken(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws NoRefreshTokenProvidedException, InvalidRefreshTokenException, IOException, ServletException {
        String refreshToken = cookieUtil.getCookieValue(request, "refresh_token").orElse(null);
        Session refreshedSession = sessionService.refreshSession(refreshToken);
        authenticate(
                refreshedSession.getUser().getId(),
                refreshedSession.getId(),
                refreshedSession.getUser().getUsername(),
                refreshedSession.getUser().getName());
        Set<ConventionalCookie> newCookies = sessionService.createSessionCookies(refreshedSession.getId());

        newCookies.stream()
                .map(ConventionalCookie::toServletCookie)
                .forEach(response::addCookie);

        filterChain.doFilter(request, response);
    }

    private void authenticate(UUID userId, UUID sessionId, String username, String name) {
        SecurityContextHolder.getContext()
                .setAuthentication(new AuthenticationToken(userId, sessionId, username, name));
    }
}
