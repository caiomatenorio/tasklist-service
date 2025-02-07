package io.github.caiomatenorio.tasklist_service.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.github.caiomatenorio.tasklist_service.convention.ConventionalResponse;
import io.github.caiomatenorio.tasklist_service.convention.SuccessResponse;
import io.github.caiomatenorio.tasklist_service.dto.request.TaskListRequest;
import io.github.caiomatenorio.tasklist_service.dto.response.TaskListResponse;
import io.github.caiomatenorio.tasklist_service.exception.TaskListNotFoundException;
import io.github.caiomatenorio.tasklist_service.exception.UnauthorizedException;
import io.github.caiomatenorio.tasklist_service.service.TaskListService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/tasklist")
@RequiredArgsConstructor
public class TaskListController {
    private final TaskListService taskListService;

    @GetMapping
    public ResponseEntity<ConventionalResponse> getAllTaskLists() throws UnauthorizedException {
        List<TaskListResponse> data = taskListService.getAllTaskLists();

        return new SuccessResponse<>(200, data).toResponseEntity();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ConventionalResponse> getTaskListById(@PathVariable Long id)
            throws UnauthorizedException, TaskListNotFoundException {
        TaskListResponse data = taskListService.getTaskListById(id);

        return new SuccessResponse<>(200, data).toResponseEntity();
    }

    @PostMapping
    public ResponseEntity<ConventionalResponse> createTaskList(@Valid @RequestBody TaskListRequest request)
            throws UnauthorizedException {
        Long id = taskListService.createTaskList(request);

        return new SuccessResponse<>(201, "Task list successfully created with id " + id)
                .toResponseEntity();
    }

    @PutMapping("/{id}")
    public ResponseEntity<ConventionalResponse> updateTaskList(@PathVariable Long id,
            @Valid @RequestBody TaskListRequest request) throws UnauthorizedException, TaskListNotFoundException {
        taskListService.updateTaskList(id, request);

        return new SuccessResponse<>(204).toResponseEntity();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ConventionalResponse> deleteTaskList(@PathVariable Long id)
            throws UnauthorizedException, TaskListNotFoundException {
        taskListService.deleteTaskList(id);

        return new SuccessResponse<>(204).toResponseEntity();
    }
}
