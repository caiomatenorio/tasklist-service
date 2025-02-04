package io.github.caiomatenorio.tasklist_service.exception;

import io.github.caiomatenorio.tasklist_service.convention.ConventionalErrorCode;
import io.github.caiomatenorio.tasklist_service.convention.ConventionalException;

public class UnauthenticatedException extends ConventionalException {
    public UnauthenticatedException() {
        super(ConventionalErrorCode.ERR004);
    }
}
