package io.github.caiomatenorio.tasklist_service.security.filter;

import java.io.IOException;
import java.util.List;
import java.util.Set;

import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.github.caiomatenorio.tasklist_service.convention.ConventionalCookie;
import io.github.caiomatenorio.tasklist_service.convention.ErrorResponse;
import io.github.caiomatenorio.tasklist_service.exception.ErrorCode;
import io.github.caiomatenorio.tasklist_service.exception.UnauthorizedException;
import io.github.caiomatenorio.tasklist_service.model.Session;
import io.github.caiomatenorio.tasklist_service.security.util.AuthUtil;
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
    private final ObjectMapper objectMapper;
    private final AuthUtil authUtil;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain) throws ServletException, IOException {
        try {
            boolean skipped = skipIfLoginOrSignup(request, response, filterChain);

            if (skipped)
                return;

            skipped = useJwt(request, response, filterChain);

            if (skipped)
                return;

            useRefreshToken(request, response, filterChain);
        } catch (UnauthorizedException e) {
            deleteAuthenticationCookies(response);
            new ErrorResponse(401, ErrorCode.ERR002).writeResponse(response, objectMapper);
        } catch (Exception e) {
            System.out.println(e);
            new ErrorResponse(500, ErrorCode.ERR000).writeResponse(response, objectMapper);
        }
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
            authUtil.authenticate(
                    jwtUtil.extractUsername(jwt),
                    jwtUtil.extractSessionId(jwt),
                    jwtUtil.extractUserId(jwt),
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
            throws UnauthorizedException, IOException, ServletException {
        String refreshToken = cookieUtil.getCookieValue(request, "refresh_token").orElse(null);
        Session refreshedSession = sessionService.refreshSession(refreshToken);
        authUtil.authenticate(
                refreshedSession.getUser().getUsername(),
                refreshedSession.getId(),
                refreshedSession.getUser().getId(),
                refreshedSession.getUser().getName());
        Set<ConventionalCookie> newCookies = sessionService.createSessionCookies(refreshedSession.getId());

        newCookies.stream()
                .map(ConventionalCookie::toServletCookie)
                .forEach(response::addCookie);

        filterChain.doFilter(request, response);
    }

    private void deleteAuthenticationCookies(HttpServletResponse response) {
        sessionService.deleteSessionCookies()
                .stream()
                .map(ConventionalCookie::toServletCookie)
                .forEach(response::addCookie);
    }
}
