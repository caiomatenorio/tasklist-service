package io.github.caiomatenorio.tasklist_service.dto.response;

import io.github.caiomatenorio.tasklist_service.model.Status;
import io.github.caiomatenorio.tasklist_service.model.Task;

public record TaskResponse(
        Long id,
        String title,
        String description,
        Status status) {
    public static TaskResponse from(Task task) {
        return new TaskResponse(
                task.getId(),
                task.getTitle(),
                task.getDescription(),
                task.getStatus());
    }
}
