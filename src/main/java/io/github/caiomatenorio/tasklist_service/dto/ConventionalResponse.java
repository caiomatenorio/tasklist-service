package io.github.caiomatenorio.tasklist_service.dto;

import java.time.Instant;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.NonNull;

public sealed interface ConventionalResponse permits ConventionalResponse.Success, ConventionalResponse.Error {
    HttpStatus getStatus();

    Instant getTimestamp();

    ConventionalResponseEntity toEntity();

    // record Success<T>(
    // @NonNull HttpStatus status,
    // @NonNull Instant timestamp,
    // T data) implements ConventionalResponse {
    // @Override
    // public ConventionalResponseEntity toEntity() {
    // return new ConventionalResponseEntity(this);
    // }
    // }

    // record Error(
    // @NonNull HttpStatus status,
    // @NonNull Instant timestamp,
    // String message) implements ConventionalResponse {
    // @Override
    // public ConventionalResponseEntity toEntity() {
    // return new ConventionalResponseEntity(this);
    // }
    // }

    @Getter
    public static final class Success<T> implements ConventionalResponse {
        private final HttpStatus status;
        private final Instant timestamp;
        private final T data;

        public Success(int status, T data) {
            this.status = HttpStatus.valueOf(status);
            this.timestamp = Instant.now();
            this.data = data;

            if (!this.status.is2xxSuccessful())
                throw new IllegalArgumentException("Success status must be 2xx");
        }

        public Success(int status) {
            this(status, null);

            if (!this.status.isSameCodeAs(HttpStatus.NO_CONTENT))
                throw new IllegalArgumentException("Success status with null data must be 204");
        }

        @Override
        public ConventionalResponseEntity toEntity() {
            return new ConventionalResponseEntity(this);
        }
    }

    @Getter
    public static final class Error implements ConventionalResponse {
        private final HttpStatus status;
        private final Instant timestamp;
        private final String message;

        public Error(int status, @NonNull String message) {
            this.status = HttpStatus.valueOf(status);
            this.timestamp = Instant.now();
            this.message = message;

            if (!(this.status.is4xxClientError() || this.status.is5xxServerError()))
                throw new IllegalArgumentException("Error status must be 4xx or 5xx");
        }

        @Override
        public ConventionalResponseEntity toEntity() {
            return new ConventionalResponseEntity(this);
        }
    }
}