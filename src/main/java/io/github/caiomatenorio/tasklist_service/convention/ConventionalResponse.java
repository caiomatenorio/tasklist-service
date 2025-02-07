package io.github.caiomatenorio.tasklist_service.convention;

import java.io.IOException;
import java.time.Instant;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpServletResponse;

public sealed interface ConventionalResponse permits SuccessResponse, ErrorResponse {
    Integer getStatusCode();

    HttpStatus getStatus();

    Instant getTimestamp();

    ResponseEntity<ConventionalResponse> toResponseEntity();

    ResponseEntity<ConventionalResponse> toResponseEntity(HttpHeaders headers);

    void writeResponse(HttpServletResponse response, ObjectMapper objectMapper) throws IOException;
}