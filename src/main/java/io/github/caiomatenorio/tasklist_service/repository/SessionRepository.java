package io.github.caiomatenorio.tasklist_service.repository;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import io.github.caiomatenorio.tasklist_service.entity.Session;

public interface SessionRepository extends JpaRepository<Session, UUID> {
    Optional<Session> findByRefreshTokenAndExpiredAtAfter(String refreshToken, Instant now);
}
