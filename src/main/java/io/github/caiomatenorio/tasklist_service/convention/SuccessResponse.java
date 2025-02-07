package io.github.caiomatenorio.tasklist_service.convention;

import java.io.IOException;
import java.time.Instant;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpServletResponse;
import lombok.Getter;

@Getter
public final class SuccessResponse<T> implements ConventionalResponse {
    private final Integer statusCode;
    private final HttpStatus status;
    private final Instant timestamp;
    private final String message;
    private final T data;

    public SuccessResponse(int statusCode, T data) {
        this.statusCode = statusCode;
        this.status = HttpStatus.valueOf(statusCode);
        this.timestamp = Instant.now();
        this.message = null;
        this.data = data;

        if (!this.status.is2xxSuccessful())
            throw new IllegalArgumentException("Success status must be 2xx");
    }

    public SuccessResponse(int statusCode, String message) {
        this.statusCode = statusCode;
        this.status = HttpStatus.valueOf(statusCode);
        this.timestamp = Instant.now();
        this.message = message;
        this.data = null;

        if (!this.status.is2xxSuccessful())
            throw new IllegalArgumentException("Success status must be 2xx");
    }

    public SuccessResponse(int statusCode) {
        this.statusCode = statusCode;
        this.status = HttpStatus.valueOf(statusCode);
        this.timestamp = null;
        this.message = null;
        this.data = null;

        if (!this.status.isSameCodeAs(HttpStatus.NO_CONTENT))
            throw new IllegalArgumentException("Success status with null data must be 204");
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