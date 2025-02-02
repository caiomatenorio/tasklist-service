package io.github.caiomatenorio.tasklist_service.entity;

import java.time.Instant;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@RequiredArgsConstructor
public class Session {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(nullable = false)
    @NonNull
    private User user;

    @Column(unique = true, nullable = false)
    @NonNull
    private String refreshToken;

    @CreationTimestamp
    @Column(nullable = false)
    @NonNull
    private Instant createdAt;

    @UpdateTimestamp
    @Column(nullable = false)
    @NonNull
    private Instant updatedAt;

    @Column(nullable = false)
    @NonNull
    private Instant expiredAt;
}
