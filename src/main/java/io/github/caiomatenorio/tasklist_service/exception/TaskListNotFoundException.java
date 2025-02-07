package io.github.caiomatenorio.tasklist_service.exception;

public class TaskListNotFoundException extends RuntimeException {
    public TaskListNotFoundException() {
        super("Task list not found");
    }
}
