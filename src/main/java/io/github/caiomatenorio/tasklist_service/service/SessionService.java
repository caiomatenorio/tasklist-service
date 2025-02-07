package io.github.caiomatenorio.tasklist_service.service;

import java.time.Instant;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.caiomatenorio.tasklist_service.convention.ConventionalCookie;
import io.github.caiomatenorio.tasklist_service.exception.UnauthorizedException;
import io.github.caiomatenorio.tasklist_service.model.Session;
import io.github.caiomatenorio.tasklist_service.model.User;
import io.github.caiomatenorio.tasklist_service.repository.SessionRepository;
import io.github.caiomatenorio.tasklist_service.security.util.JwtUtil;
import io.github.caiomatenorio.tasklist_service.security.util.RefreshTokenUtil;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SessionService {
    @Value("${app.session.expirationSeconds}")
    private int sessionExpirationSeconds;

    private final SessionRepository sessionRepository;
    private final RefreshTokenUtil refreshTokenUtil;
    private final JwtUtil jwtUtil;

    @Transactional(readOnly = true)
    public Session getSession(UUID sessionId) throws NoSuchElementException {
        return sessionRepository.findById(sessionId).orElseThrow();
    }

    public UUID createSession(User user) {
        String refreshToken = refreshTokenUtil.generateRefreshToken();
        Instant now = Instant.now();
        Instant expiration = now.plusSeconds(sessionExpirationSeconds);

        Session session = new Session(user, refreshToken, now, now, expiration);
        return sessionRepository.save(session).getId();
    }

    @Transactional
    public Session refreshSession(@Nullable String refreshToken)
            throws UnauthorizedException {
        if (refreshToken == null)
            throw new UnauthorizedException();

        Session session = sessionRepository
                .findByRefreshTokenAndExpiredAtAfter(refreshToken, Instant.now())
                .orElseThrow(UnauthorizedException::new);

        String newRefreshToken = refreshTokenUtil.generateRefreshToken();
        Instant now = Instant.now();
        Instant newExpiration = now.plusSeconds(sessionExpirationSeconds);

        session.setRefreshToken(newRefreshToken);
        session.setUpdatedAt(now);
        session.setExpiredAt(newExpiration);

        return sessionRepository.save(session);
    }

    @Transactional(readOnly = true)
    public Set<ConventionalCookie> createSessionCookies(UUID sessionId) throws NoSuchElementException {
        Session session = sessionRepository.findById(sessionId).orElseThrow();

        String jwt = jwtUtil.generateJwt(
                session.getId(),
                session.getUser().getId(),
                session.getUser().getUsername(),
                session.getUser().getName());
        String refreshToken = session.getRefreshToken();

        ConventionalCookie authCookie = jwtUtil.createAuthCookie(jwt);
        ConventionalCookie refreshCookie = refreshTokenUtil.createRefreshCookie(refreshToken);

        return Set.of(authCookie, refreshCookie);
    }

    public Set<ConventionalCookie> deleteSessionCookies() {
        ConventionalCookie deletedAuthCookie = jwtUtil.deleteAuthCookie();
        ConventionalCookie deletedRefreshCookie = refreshTokenUtil.deleteRefreshCookie();

        return Set.of(deletedAuthCookie, deletedRefreshCookie);
    }

    @Transactional
    public void deleteSession(UUID sessionId) {
        sessionRepository.deleteById(sessionId);
    }
}
