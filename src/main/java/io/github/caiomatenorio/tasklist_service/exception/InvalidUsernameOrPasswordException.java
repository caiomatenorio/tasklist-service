package io.github.caiomatenorio.tasklist_service.exception;

import io.github.caiomatenorio.tasklist_service.convention.ConventionalErrorCode;
import io.github.caiomatenorio.tasklist_service.convention.ConventionalException;

public class InvalidUsernameOrPasswordException extends ConventionalException {
    public InvalidUsernameOrPasswordException() {
        super(ConventionalErrorCode.ERR002);
    }
}
