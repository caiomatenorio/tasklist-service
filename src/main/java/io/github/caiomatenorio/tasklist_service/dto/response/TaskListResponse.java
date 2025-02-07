package io.github.caiomatenorio.tasklist_service.dto.response;

import io.github.caiomatenorio.tasklist_service.model.TaskList;

public record TaskListResponse(
        Long id,
        String name,
        String description) {
    public static TaskListResponse from(TaskList taskList) {
        return new TaskListResponse(
                taskList.getId(),
                taskList.getName(),
                taskList.getDescription());
    }
}
