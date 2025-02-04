package io.github.caiomatenorio.tasklist_service.exception;

import io.github.caiomatenorio.tasklist_service.convention.ConventionalErrorCode;
import io.github.caiomatenorio.tasklist_service.convention.ConventionalException;

public class UsernameAlreadyInUseException extends ConventionalException {
    public UsernameAlreadyInUseException() {
        super(ConventionalErrorCode.ERR005);
    }
}
