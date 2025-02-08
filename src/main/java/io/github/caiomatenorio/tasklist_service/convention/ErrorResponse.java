package io.github.caiomatenorio.tasklist_service.convention;

import java.io.IOException;
import java.time.Instant;
import java.util.Map;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.github.caiomatenorio.tasklist_service.exception.ErrorCode;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Getter;
import lombok.NonNull;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public final class ErrorResponse implements ConventionalResponse {
    private final Integer statusCode;
    private final HttpStatus status;
    private final Instant timestamp;
    private final ErrorCode errorCode;
    private final String message;
    private final Map<String, String> errors;

    public ErrorResponse(int statusCode, @NonNull ErrorCode errorCode) {
        this(statusCode, errorCode, errorCode.getMessage());
    }

    public ErrorResponse(int statusCode, @NonNull ErrorCode errorCode, @NonNull String message) {
        this.statusCode = statusCode;
        this.status = HttpStatus.valueOf(statusCode);
        this.timestamp = Instant.now();
        this.errorCode = errorCode;
        this.message = message;
        this.errors = null;

        if (!status.isError())
            throw new IllegalArgumentException("Error status must be 4xx or 5xx");
    }

    public ErrorResponse(int statusCode, @NonNull ErrorCode errorCode, @NonNull Map<String, String> errors) {
        this(statusCode, errorCode, errorCode.getMessage(), errors);
    }

    public ErrorResponse(
            int statusCode,
            @NonNull ErrorCode errorCode,
            @NonNull String message,
            @NonNull Map<String, String> errors) {
        this.statusCode = statusCode;
        this.status = HttpStatus.valueOf(statusCode);
        this.timestamp = Instant.now();
        this.errorCode = errorCode;
        this.message = message;
        this.errors = errors;

        if (!status.isError())
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
