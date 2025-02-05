package io.github.caiomatenorio.tasklist_service.exception;

public class UnauthorizedException extends RuntimeException {
    public UnauthorizedException() {
        super("The user is not authorized to perform this action.");
    }
}
