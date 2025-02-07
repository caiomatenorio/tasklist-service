package io.github.caiomatenorio.tasklist_service.model;

import io.github.caiomatenorio.tasklist_service.exception.InvalidStatusException;

public enum Status {
    DONE, IN_PROGRESS, TODO;

    public static Status from(String s) throws InvalidStatusException {
        switch (s) {
            case "todo":
                return TODO;
            case "in_progress":
                return IN_PROGRESS;
            case "done":
                return DONE;
            default:
                throw new InvalidStatusException();
        }
    }
}
