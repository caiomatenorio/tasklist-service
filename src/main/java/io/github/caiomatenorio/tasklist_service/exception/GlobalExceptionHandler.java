package io.github.caiomatenorio.tasklist_service.exception;

import java.util.Set;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import io.github.caiomatenorio.tasklist_service.convention.ConventionalCookie;
import io.github.caiomatenorio.tasklist_service.convention.ConventionalErrorCode;
import io.github.caiomatenorio.tasklist_service.convention.ConventionalException;
import io.github.caiomatenorio.tasklist_service.convention.ConventionalResponseBody;
import io.github.caiomatenorio.tasklist_service.util.CookieUtil;
import lombok.RequiredArgsConstructor;

@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {
    private final CookieUtil cookieUtil;

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ConventionalResponseBody> handleGenericException(Exception e) {
        return new ConventionalResponseBody.Error(500, ConventionalErrorCode.ERR000).toResponseEntity();
    }

    @ExceptionHandler(ConventionalException.class)
    public ResponseEntity<ConventionalResponseBody> handleConventionalException(ConventionalException e) {
        return new ConventionalResponseBody.Error(e.getHttpStatus(), e.getErrorCode()).toResponseEntity();
    }

    @ExceptionHandler(InvalidRefreshTokenException.class)
    public ResponseEntity<ConventionalResponseBody> handleInvalidRefreshTokenException(InvalidRefreshTokenException e) {
        Set<ConventionalCookie> deletedCookies = cookieUtil.deleteCookies("auth_token", "refresh_token");
        HttpHeaders headers = cookieUtil.toHttpHeaders(deletedCookies);

        return new ConventionalResponseBody.Error(e.getHttpStatus(), e.getErrorCode()).toResponseEntity(headers);
    }
}
