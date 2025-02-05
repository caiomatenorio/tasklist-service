package io.github.caiomatenorio.tasklist_service.exception;

import java.util.Set;

import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import io.github.caiomatenorio.tasklist_service.convention.ConventionalCookie;
import io.github.caiomatenorio.tasklist_service.convention.ConventionalResponseBody;
import io.github.caiomatenorio.tasklist_service.service.SessionService;
import io.github.caiomatenorio.tasklist_service.util.CookieUtil;
import lombok.RequiredArgsConstructor;

@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {
    private final CookieUtil cookieUtil;
    private final SessionService sessionService;

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ConventionalResponseBody> handleException(Exception e) {
        System.out.println(e.toString()); // TODO: remove
        return new ConventionalResponseBody.Error(500, ErrorCode.ERR000).toResponseEntity();
    }

    @ExceptionHandler(InvalidUsernameOrPasswordException.class)
    public ResponseEntity<ConventionalResponseBody> handleInvalidUsernameOrPasswordException(
            InvalidUsernameOrPasswordException e) {
        return new ConventionalResponseBody.Error(401, ErrorCode.ERR001).toResponseEntity();
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ConventionalResponseBody> handleUnauthorizedException(UnauthorizedException e) {
        Set<ConventionalCookie> deletedCookies = sessionService.deleteSessionCookies();
        var headers = cookieUtil.toHttpHeaders(deletedCookies);

        return new ConventionalResponseBody.Error(401, ErrorCode.ERR002).toResponseEntity(headers);
    }

    @ExceptionHandler(UsernameAlreadyInUseException.class)
    public ResponseEntity<ConventionalResponseBody> handleUsernameAlreadyInUseException(
            UsernameAlreadyInUseException e) {
        return new ConventionalResponseBody.Error(409, ErrorCode.ERR003).toResponseEntity();
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ConventionalResponseBody> handleHttpRequestMethodNotSupportedException(
            HttpRequestMethodNotSupportedException e) {
        return new ConventionalResponseBody.Error(405, ErrorCode.ERR004).toResponseEntity();
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<ConventionalResponseBody> handleNoResourceFoundException(NoResourceFoundException e) {
        return new ConventionalResponseBody.Error(404, ErrorCode.ERR005).toResponseEntity();
    }

    public ResponseEntity<ConventionalResponseBody> handleInvalidPasswordException(InvalidPasswordException e) {
        return new ConventionalResponseBody.Error(400, ErrorCode.ERR006).toResponseEntity();
    }
}
