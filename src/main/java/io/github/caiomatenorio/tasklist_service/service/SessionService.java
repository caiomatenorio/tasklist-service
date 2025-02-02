package io.github.caiomatenorio.tasklist_service.service;

import java.time.Instant;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;

import io.github.caiomatenorio.tasklist_service.dto.output.RefreshSessionOutput;
import io.github.caiomatenorio.tasklist_service.entity.Session;
import io.github.caiomatenorio.tasklist_service.exception.RefreshTokenException;
import io.github.caiomatenorio.tasklist_service.repository.SessionRepository;
import io.github.caiomatenorio.tasklist_service.util.JwtUtil;
import io.github.caiomatenorio.tasklist_service.util.RefreshTokenUtil;
import jakarta.servlet.http.Cookie;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SessionService {
    @Value("${app.session.expirationSeconds}")
    private int sessionExpirationSeconds;

    private final SessionRepository sessionRepository;
    private final RefreshTokenUtil refreshTokenUtil;
    private final JwtUtil jwtUtil;

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

    public Set<Cookie> createSessionCookies(UUID sessionId) throws NoSuchElementException {
        Session session = sessionRepository.findById(sessionId).orElseThrow();

        String jwt = jwtUtil.generateJwt(session.getUser().getUsername());
        String refreshToken = session.getRefreshToken();

        Cookie authCookie = jwtUtil.createAuthCookie(jwt);
        Cookie refreshCookie = refreshTokenUtil.createRefreshCookie(refreshToken);

        return Set.of(authCookie, refreshCookie);
    }
}
