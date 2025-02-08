package io.github.caiomatenorio.tasklist_service.exception;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import io.github.caiomatenorio.tasklist_service.convention.ConventionalCookie;
import io.github.caiomatenorio.tasklist_service.convention.ConventionalResponse;
import io.github.caiomatenorio.tasklist_service.convention.ErrorResponse;
import io.github.caiomatenorio.tasklist_service.service.SessionService;
import io.github.caiomatenorio.tasklist_service.util.CookieUtil;
import lombok.RequiredArgsConstructor;

@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {
    private final CookieUtil cookieUtil;
    private final SessionService sessionService;

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ConventionalResponse> handleException(Exception e) {
        System.out.println(e.toString()); // TODO: remove
        return new ErrorResponse(500, ErrorCode.ERR000).toResponseEntity();
    }

    @ExceptionHandler(InvalidUsernameOrPasswordException.class)
    public ResponseEntity<ConventionalResponse> handleInvalidUsernameOrPasswordException(
            InvalidUsernameOrPasswordException e) {
        return new ErrorResponse(401, ErrorCode.ERR001).toResponseEntity();
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ConventionalResponse> handleUnauthorizedException(UnauthorizedException e) {
        Set<ConventionalCookie> deletedCookies = sessionService.deleteSessionCookies();
        var headers = cookieUtil.toHttpHeaders(deletedCookies);

        return new ErrorResponse(401, ErrorCode.ERR002).toResponseEntity(headers);
    }

    @ExceptionHandler(UsernameAlreadyInUseException.class)
    public ResponseEntity<ConventionalResponse> handleUsernameAlreadyInUseException(
            UsernameAlreadyInUseException e) {
        return new ErrorResponse(409, ErrorCode.ERR003).toResponseEntity();
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ConventionalResponse> handleHttpRequestMethodNotSupportedException(
            HttpRequestMethodNotSupportedException e) {
        return new ErrorResponse(405, ErrorCode.ERR004).toResponseEntity();
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<ConventionalResponse> handleNoResourceFoundException(NoResourceFoundException e) {
        return new ErrorResponse(404, ErrorCode.ERR005).toResponseEntity();
    }

    @ExceptionHandler(InvalidPasswordException.class)
    public ResponseEntity<ConventionalResponse> handleInvalidPasswordException(InvalidPasswordException e) {
        return new ErrorResponse(400, ErrorCode.ERR006).toResponseEntity();
    }

    @ExceptionHandler(TaskListNotFoundException.class)
    public ResponseEntity<ConventionalResponse> handleTaskListNotFoundException(TaskListNotFoundException e) {
        return new ErrorResponse(404, ErrorCode.ERR007).toResponseEntity();
    }

    @ExceptionHandler(TaskNotFoundException.class)
    public ResponseEntity<ConventionalResponse> handleTaskNotFoundException(TaskNotFoundException e) {
        return new ErrorResponse(404, ErrorCode.ERR008).toResponseEntity();
    }

    @ExceptionHandler(InvalidStatusException.class)
    public ResponseEntity<ConventionalResponse> handleInvalidStatusException(InvalidStatusException e) {
        return new ErrorResponse(400, ErrorCode.ERR009).toResponseEntity();
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ConventionalResponse> handleMethodArgumentNotValidException(
            MethodArgumentNotValidException e) {
        Map<String, String> errors = new HashMap<>();

        e.getBindingResult().getFieldErrors().forEach(error -> {
            String message = errors.get(error.getField());

            if (message == null) {
                errors.put(error.getField(), error.getDefaultMessage());
                return;
            }

            message += " " + error.getDefaultMessage();
            errors.put(error.getField(), message);
        });

        return new ErrorResponse(400, ErrorCode.ERR010, errors).toResponseEntity();
    }
}
