package io.github.caiomatenorio.tasklist_service.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.github.caiomatenorio.tasklist_service.convention.ConventionalResponse;
import io.github.caiomatenorio.tasklist_service.convention.SuccessResponse;
import io.github.caiomatenorio.tasklist_service.dto.request.TaskRequest;
import io.github.caiomatenorio.tasklist_service.dto.response.TaskResponse;
import io.github.caiomatenorio.tasklist_service.exception.TaskListNotFoundException;
import io.github.caiomatenorio.tasklist_service.exception.TaskNotFoundException;
import io.github.caiomatenorio.tasklist_service.exception.UnauthorizedException;
import io.github.caiomatenorio.tasklist_service.service.TaskService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/tasklist/{taskListId}/tasks")
@Validated
@RequiredArgsConstructor
public class TaskController {
    private final TaskService taskService;

    @GetMapping
    public ResponseEntity<ConventionalResponse> getTasksFromTaskList(@PathVariable Long taskListId,
            @RequestParam(required = false) String status) {
        List<TaskResponse> tasks;

        if (status == null) {
            tasks = taskService.getAllTasksFromTaskList(taskListId);
        } else {
            tasks = taskService.getTaskByStatusFromTaskList(taskListId, status);
        }

        return new SuccessResponse<>(200, tasks).toResponseEntity();
    }

    @GetMapping("/{taskId}")
    public ResponseEntity<ConventionalResponse> getTaskByIdFromTaskList(@PathVariable Long taskListId,
            @PathVariable Long taskId) throws UnauthorizedException, TaskNotFoundException {
        TaskResponse task = taskService.getTaskByIdFromTaskList(taskListId, taskId);

        return new SuccessResponse<>(200, task).toResponseEntity();
    }

    @PostMapping
    public ResponseEntity<ConventionalResponse> createTaskForTaskList(@PathVariable Long taskListId,
            @Valid @RequestBody TaskRequest request) throws UnauthorizedException, TaskNotFoundException {
        Long id = taskService.createTaskForTaskList(taskListId, request);

        return new SuccessResponse<>(201, "Task created with id " + id).toResponseEntity();
    }

    @PutMapping("/{taskId}")
    public ResponseEntity<ConventionalResponse> updateTaskFromTaskList(@PathVariable Long taskListId,
            @PathVariable Long taskId, @Valid @RequestBody TaskRequest request)
            throws UnauthorizedException, TaskListNotFoundException, TaskNotFoundException {
        taskService.updateTaskFromTaskList(taskListId, taskId, request);

        return new SuccessResponse<>(204).toResponseEntity();
    }

    @PatchMapping("/{taskId}")
    public ResponseEntity<ConventionalResponse> updateTaskStatusFromTaskList(@PathVariable Long taskListId,
            @PathVariable Long taskId, @RequestParam String status)
            throws UnauthorizedException, TaskListNotFoundException, TaskNotFoundException {
        taskService.updateTaskStatusFromTaskList(taskListId, taskId, status);

        return new SuccessResponse<>(204).toResponseEntity();
    }

    @DeleteMapping("/{taskId}")
    public ResponseEntity<ConventionalResponse> deleteTaskFromTaskList(@PathVariable Long taskListId,
            @PathVariable Long taskId) throws UnauthorizedException, TaskListNotFoundException, TaskNotFoundException {
        taskService.deleteTaskFromTaskList(taskListId, taskId);

        return new SuccessResponse<>(204).toResponseEntity();
    }
}
