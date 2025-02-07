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

@Getter
public final class ErrorResponse implements ConventionalResponse {
    private final Integer statusCode;
    private final HttpStatus status;
    private final Instant timestamp;
    private final ErrorCode errorCode;
    private final String message;

    public ErrorResponse(int statusCode, @NonNull ErrorCode errorCode) {
        this(statusCode, errorCode, errorCode.getMessage());
    }

    public ErrorResponse(int statusCode, @NonNull ErrorCode errorCode, @NonNull String message) {
        this.statusCode = statusCode;
        this.status = HttpStatus.valueOf(statusCode);
        this.timestamp = Instant.now();
        this.errorCode = errorCode;
        this.message = message;

        if (!(this.status.is4xxClientError() || this.status.is5xxServerError()))
            throw new IllegalArgumentException("Error status must be 4xx or 5xx");
    }

    @Override
    public ResponseEntity<ConventionalResponse> toResponseEntity() {
        return ResponseEntity.status(status).body(this);
    }

    @Override
    public ResponseEntity<ConventionalResponse> toResponseEntity(HttpHeaders headers) {
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
