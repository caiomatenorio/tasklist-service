package io.github.caiomatenorio.tasklist_service.service;

import java.time.Instant;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;

import io.github.caiomatenorio.tasklist_service.convention.ConventionalCookie;
import io.github.caiomatenorio.tasklist_service.dto.RefreshSessionOutput;
import io.github.caiomatenorio.tasklist_service.entity.Session;
import io.github.caiomatenorio.tasklist_service.entity.User;
import io.github.caiomatenorio.tasklist_service.exception.RefreshTokenException;
import io.github.caiomatenorio.tasklist_service.repository.SessionRepository;
import io.github.caiomatenorio.tasklist_service.util.JwtUtil;
import io.github.caiomatenorio.tasklist_service.util.RefreshTokenUtil;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SessionService {
    @Value("${app.session.expirationSeconds}")
    private int sessionExpirationSeconds;

    private final SessionRepository sessionRepository;
    private final RefreshTokenUtil refreshTokenUtil;
    private final JwtUtil jwtUtil;

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

    public RefreshSessionOutput refreshSession(@Nullable String refreshToken) throws RefreshTokenException {
        if (refreshToken == null)
            throw new RefreshTokenException.NoRefreshTokenProvidedException();

        Session session = sessionRepository
                .findByRefreshTokenAndExpiredAtAfter(refreshToken, Instant.now())
                .orElseThrow(RefreshTokenException.InvalidRefreshTokenException::new);

        String newRefreshToken = refreshTokenUtil.generateRefreshToken();
        Instant now = Instant.now();
        Instant newExpiration = now.plusSeconds(sessionExpirationSeconds);

        session.setRefreshToken(newRefreshToken);
        session.setUpdatedAt(now);
        session.setExpiredAt(newExpiration);

        Session updatedSession = sessionRepository.save(session);

        return new RefreshSessionOutput(updatedSession.getId(), updatedSession.getUser().getUsername());
    }

    public Set<ConventionalCookie> createSessionCookies(UUID sessionId) throws NoSuchElementException {
        Session session = sessionRepository.findById(sessionId).orElseThrow();

        String jwt = jwtUtil.generateJwt(session.getId());
        String refreshToken = session.getRefreshToken();

        ConventionalCookie authCookie = jwtUtil.createAuthCookie(jwt);
        ConventionalCookie refreshCookie = refreshTokenUtil.createRefreshCookie(refreshToken);

        return Set.of(authCookie, refreshCookie);
    }

    public void deleteSession(UUID sessionId) {
        sessionRepository.deleteById(sessionId);
    }
}
