package io.github.caiomatenorio.tasklist_service.exception;

import io.github.caiomatenorio.tasklist_service.convention.ConventionalErrorCode;
import io.github.caiomatenorio.tasklist_service.convention.ConventionalException;

public class InvalidRefreshTokenException extends ConventionalException {
    public InvalidRefreshTokenException() {
        super(ConventionalErrorCode.ERR001);
    }
}
