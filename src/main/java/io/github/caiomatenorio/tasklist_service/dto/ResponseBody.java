package io.github.caiomatenorio.tasklist_service.dto;

import java.time.Instant;

import org.springframework.http.HttpStatus;

import lombok.NonNull;

public sealed interface ResponseBody permits ResponseBody.Success, ResponseBody.Error {
        HttpStatus status();

        Instant timestamp();

        record Success<T>(
                        @NonNull HttpStatus status,
                        @NonNull Instant timestamp,
                        T data) implements ResponseBody {
        }

        record Error(
                        @NonNull HttpStatus status,
                        @NonNull Instant timestamp,
                        String message) implements ResponseBody {
        }
}