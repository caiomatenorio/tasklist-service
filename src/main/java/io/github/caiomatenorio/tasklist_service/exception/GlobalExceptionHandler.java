package io.github.caiomatenorio.tasklist_service.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import io.github.caiomatenorio.tasklist_service.convention.ConventionalResponseBody;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ConventionalResponseBody> handleGenericException(Exception e) {
        return ResponseEntity
                .internalServerError()
                .body(new ConventionalResponseBody.Error(500, "An unexpected error occurred"));
    }

    @ExceptionHandler(UsernameAlreadyInUseException.class)
    public ResponseEntity<ConventionalResponseBody> handleUsernameAlreadyInUseException(
            UsernameAlreadyInUseException e) {
        return ResponseEntity
                .badRequest()
                .body(new ConventionalResponseBody.Error(400, e.getMessage()));
    }

    @ExceptionHandler(InvalidUsernameOrPasswordException.class)
    public ResponseEntity<ConventionalResponseBody> handleInvalidUsernameOrPasswordException(
            InvalidUsernameOrPasswordException e) {
        return ResponseEntity
                .badRequest()
                .body(new ConventionalResponseBody.Error(400, e.getMessage()));
    }
}
