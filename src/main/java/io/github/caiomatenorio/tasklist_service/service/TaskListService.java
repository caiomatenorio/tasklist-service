package io.github.caiomatenorio.tasklist_service.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.caiomatenorio.tasklist_service.dto.request.TaskListRequest;
import io.github.caiomatenorio.tasklist_service.dto.response.TaskListResponse;
import io.github.caiomatenorio.tasklist_service.exception.TaskListNotFoundException;
import io.github.caiomatenorio.tasklist_service.exception.UnauthorizedException;
import io.github.caiomatenorio.tasklist_service.model.TaskList;
import io.github.caiomatenorio.tasklist_service.model.User;
import io.github.caiomatenorio.tasklist_service.repository.TaskListRepository;
import io.github.caiomatenorio.tasklist_service.security.util.AuthUtil;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TaskListService {
    private final TaskListRepository taskListRepository;
    private final AuthUtil authUtil;
    private final UserService userService;

    /**
     * Retrieves all task lists owned by the current user.
     * 
     * @return a list of DTOs containing id, name and description of the task lists.
     * @throws UnauthorizedException when the user is not correctly authenticated.
     */
    @Transactional(readOnly = true)
    public List<TaskListResponse> getAllTaskLists() throws UnauthorizedException {
        String username = authUtil.getCurrentUsername();
        List<TaskList> taskLists = taskListRepository.findAll(username);

        return taskLists.stream().map(TaskListResponse::from).toList();
    }

    /**
     * Retrieves the information of the task list with the specified id.
     * 
     * @param id the id of the task list.
     * @return a DTO containing id, name and description of the task list.
     * @throws UnauthorizedException     when the user is not correctly
     *                                   authenticated.
     * @throws TaskListNotFoundException when the task list does not exist or is not
     *                                   owned by the current user.
     */
    @Transactional(readOnly = true)
    public TaskListResponse getTaskListById(Long id) throws UnauthorizedException, TaskListNotFoundException {
        String username = authUtil.getCurrentUsername();
        TaskList taskList = taskListRepository.findById(id, username).orElseThrow(TaskListNotFoundException::new);

        return TaskListResponse.from(taskList);
    }

    /**
     * Creates a new task list.
     * 
     * @param request the DTO containing the data for the creating the new task
     *                list.
     * @return the id of the new task list.
     * @throws UnauthorizedException when the user is not correctly authenticated.
     */
    @Transactional
    public Long createTaskList(TaskListRequest request) throws UnauthorizedException {
        String username = authUtil.getCurrentUsername();
        User user = userService.getUserByUsername(username);
        var taskList = new TaskList(request.name(), request.description(), user);
        Long taskListId = taskListRepository.save(taskList).getId();

        return taskListId;
    }

    /**
     * Updates the specified task list information.
     * 
     * @param id      the task list id.
     * @param request the DTO containing the data for updating the task list
     *                information.
     * @throws UnauthorizedException     when the user is not correctly
     *                                   authenticated.
     * @throws TaskListNotFoundException when the task list does not exist or is not
     *                                   owned by the current user.
     */
    @Transactional
    public void updateTaskList(Long id, TaskListRequest request)
            throws UnauthorizedException, TaskListNotFoundException {
        String username = authUtil.getCurrentUsername();
        TaskList taskList = taskListRepository.findById(id, username).orElseThrow(TaskListNotFoundException::new);

        taskList.setName(request.name());
        taskList.setDescription(request.description());

        taskListRepository.save(taskList);
    }

    /**
     * Deletes the specified task list.
     * 
     * @param id the task list id.
     * @throws UnauthorizedException     when the user is not correctly
     *                                   authenticated.
     * @throws TaskListNotFoundException when the task list does not exist or is not
     *                                   owned by the current user.
     */
    @Transactional
    public void deleteTaskList(Long id) throws UnauthorizedException, TaskListNotFoundException {
        String username = authUtil.getCurrentUsername();
        TaskList taskList = taskListRepository.findById(id, username).orElseThrow(TaskListNotFoundException::new);

        taskListRepository.delete(taskList);
    }

    /**
     * Validates if the specified task list exists and is owned by the current user.
     * Must be used by a transaction.
     * 
     * @param id the task list id.
     * @throws UnauthorizedException     when the user is not correctly
     *                                   authenticated.
     * @throws TaskListNotFoundException when the task list does not exist or is not
     *                                   owned by the current user.
     */
    public void validateTaskList(Long id) throws UnauthorizedException, TaskListNotFoundException {
        String username = authUtil.getCurrentUsername();
        boolean exists = taskListRepository.taskListExists(id, username);

        if (!exists)
            throw new TaskListNotFoundException();
    }
}
