package io.github.caiomatenorio.tasklist_service.exception;

public class UsernameAlreadyInUseException extends RuntimeException {
    public UsernameAlreadyInUseException() {
        super("The username is already in use.");
    }
}
