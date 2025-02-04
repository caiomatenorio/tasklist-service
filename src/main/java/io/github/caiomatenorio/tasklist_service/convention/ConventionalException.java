package io.github.caiomatenorio.tasklist_service.convention;

import lombok.Getter;

@Getter
public class ConventionalException extends RuntimeException {
    private ConventionalErrorCode errorCode;
    private int httpStatus;

    public ConventionalException(ConventionalErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
        this.httpStatus = errorCode.getHttpStatus();
    }
}
