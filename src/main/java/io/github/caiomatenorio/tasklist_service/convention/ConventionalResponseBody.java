package io.github.caiomatenorio.tasklist_service.convention;

import java.time.Instant;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import lombok.Getter;
import lombok.NonNull;

public sealed interface ConventionalResponseBody
        permits ConventionalResponseBody.Success, ConventionalResponseBody.Error {
    HttpStatus getStatus();

    Instant getTimestamp();

    ResponseEntity<ConventionalResponseBody> toResponseEntity();

    ResponseEntity<ConventionalResponseBody> toResponseEntity(HttpHeaders headers);

    @Getter
    public static final class Success<T> implements ConventionalResponseBody {
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
        public ResponseEntity<ConventionalResponseBody> toResponseEntity() {
            return ResponseEntity.status(status).body(this);
        }

        @Override
        public ResponseEntity<ConventionalResponseBody> toResponseEntity(HttpHeaders headers) {
            return ResponseEntity.status(status).headers(headers).body(this);
        }
    }

    @Getter
    public static final class Error implements ConventionalResponseBody {
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
        public ResponseEntity<ConventionalResponseBody> toResponseEntity() {
            return ResponseEntity.status(status).body(this);
        }

        @Override
        public ResponseEntity<ConventionalResponseBody> toResponseEntity(HttpHeaders headers) {
            return ResponseEntity.status(status).headers(headers).body(this);
        }
    }
}