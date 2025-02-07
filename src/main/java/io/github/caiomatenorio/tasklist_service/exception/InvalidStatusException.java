package io.github.caiomatenorio.tasklist_service.exception;

public class InvalidStatusException extends RuntimeException {
    public InvalidStatusException() {
        super("The given status is invalid. It should be 'todo', 'in_progress' or 'done'.");
    }
}
