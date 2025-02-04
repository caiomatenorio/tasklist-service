package io.github.caiomatenorio.tasklist_service.convention;

import org.springframework.http.HttpStatus;

import lombok.Getter;

@Getter
public enum ConventionalErrorCode {
    ERR000(500, "ERR000", "Internal server error"),
    ERR001(401, "ERR001", "Invalid refresh token"),
    ERR002(401, "ERR002", "Invalid username or password"),
    ERR003(401, "ERR003", "No authentication token provided"),
    ERR004(401, "ERR004", "The user is not authenticated"),
    ERR005(409, "ERR005", "Username already in use"),;

    private final int httpStatus;
    private final String code;
    private final String message;

    ConventionalErrorCode(int httpStatus, String code, String message) {
        if (!HttpStatus.valueOf(httpStatus).isError())
            throw new IllegalArgumentException("Error status must be 4xx or 5xx");

        this.httpStatus = httpStatus;
        this.code = code;
        this.message = message;
    }
}
