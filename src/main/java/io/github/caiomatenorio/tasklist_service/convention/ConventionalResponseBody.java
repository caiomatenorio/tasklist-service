package io.github.caiomatenorio.tasklist_service.convention;

import java.io.IOException;
import java.time.Instant;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.github.caiomatenorio.tasklist_service.exception.ErrorCode;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Getter;
import lombok.NonNull;

public sealed interface ConventionalResponseBody
        permits ConventionalResponseBody.Success, ConventionalResponseBody.Error {
    Integer getStatusCode();

    HttpStatus getStatus();

    Instant getTimestamp();

    ResponseEntity<ConventionalResponseBody> toResponseEntity();

    ResponseEntity<ConventionalResponseBody> toResponseEntity(HttpHeaders headers);

    void writeResponse(HttpServletResponse response, ObjectMapper objectMapper) throws IOException;

    @Getter
    public static final class Success<T> implements ConventionalResponseBody {
        private final Integer statusCode;
        private final HttpStatus status;
        private final Instant timestamp;
        private final T data;

        public Success(int statusCode, T data) {
            this.statusCode = statusCode;
            this.status = HttpStatus.valueOf(statusCode);
            this.timestamp = Instant.now();
            this.data = data;

            if (!this.status.is2xxSuccessful())
                throw new IllegalArgumentException("Success status must be 2xx");
        }

        public Success(int statusCode) {
            this.statusCode = statusCode;
            this.status = HttpStatus.valueOf(statusCode);
            this.timestamp = null;
            this.data = null;

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

        @Override
        public void writeResponse(HttpServletResponse response, ObjectMapper objectMapper) throws IOException {
            response.setStatus(statusCode);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");

            objectMapper.writeValue(response.getWriter(), this);
        }
    }

    @Getter
    public static final class Error implements ConventionalResponseBody {
        private final Integer statusCode;
        private final HttpStatus status;
        private final Instant timestamp;
        private final ErrorCode errorCode;
        private final String message;

        public Error(int statusCode, @NonNull ErrorCode errorCode) {
            this(statusCode, errorCode, errorCode.getMessage());
        }

        public Error(int statusCode, @NonNull ErrorCode errorCode, @NonNull String message) {
            this.statusCode = statusCode;
            this.status = HttpStatus.valueOf(statusCode);
            this.timestamp = Instant.now();
            this.errorCode = errorCode;
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

        @Override
        public void writeResponse(HttpServletResponse response, ObjectMapper objectMapper) throws IOException {
            response.setStatus(statusCode);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");

            objectMapper.writeValue(response.getWriter(), this);
        }
    }
}