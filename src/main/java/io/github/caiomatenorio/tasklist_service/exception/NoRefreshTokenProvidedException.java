package io.github.caiomatenorio.tasklist_service.exception;

import io.github.caiomatenorio.tasklist_service.convention.ConventionalErrorCode;
import io.github.caiomatenorio.tasklist_service.convention.ConventionalException;

public class NoRefreshTokenProvidedException extends ConventionalException {
    public NoRefreshTokenProvidedException() {
        super(ConventionalErrorCode.ERR003);
    }
}
