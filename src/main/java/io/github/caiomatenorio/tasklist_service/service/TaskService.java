package io.github.caiomatenorio.tasklist_service.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.caiomatenorio.tasklist_service.dto.request.TaskRequest;
import io.github.caiomatenorio.tasklist_service.dto.response.TaskResponse;
import io.github.caiomatenorio.tasklist_service.exception.TaskListNotFoundException;
import io.github.caiomatenorio.tasklist_service.exception.TaskNotFoundException;
import io.github.caiomatenorio.tasklist_service.exception.UnauthorizedException;
import io.github.caiomatenorio.tasklist_service.model.Status;
import io.github.caiomatenorio.tasklist_service.model.Task;
import io.github.caiomatenorio.tasklist_service.repository.TaskRepository;
import io.github.caiomatenorio.tasklist_service.security.util.AuthUtil;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TaskService {
	private final AuthUtil authUtil;
	private final TaskRepository taskRepository;
	private final TaskListService taskListService;

	/**
	 * Retrieves all tasks in the specified task list.
	 * 
	 * @param taskListId the id of the task list.
	 * @return a list of DTOs with id, title, description and status of the tasks.
	 * @throws UnauthorizedException     when the user is not correctly
	 *                                   authenticated.
	 * @throws TaskListNotFoundException when the task list does not exist or is not
	 *                                   owned by the current user.
	 */
	@Transactional(readOnly = true)
	public List<TaskResponse> getAllTasksFromTaskList(Long taskListId)
			throws UnauthorizedException, TaskListNotFoundException {
		taskListService.validateTaskList(taskListId);

		String username = authUtil.getCurrentUsername();
		List<Task> tasks = taskRepository.findByTaskListId(taskListId, username);

		return tasks.stream().map(TaskResponse::from).toList();
	}

	/**
	 * Retrieves the tasks in the specified task list filtered by the specified
	 * status.
	 * 
	 * @param taskListId the task list id.
	 * @param status     the status to filter the tasks.
	 * @return a list of DTOs with id, title, description and status of the tasks.
	 * @throws UnauthorizedException     when the user is not correctly
	 *                                   authenticated.
	 * @throws TaskListNotFoundException when the task list does not exist or is not
	 *                                   owned by the current user.
	 */
	@Transactional(readOnly = true)
	public List<TaskResponse> getTaskByStatusFromTaskList(Long taskListId, String statusString)
			throws UnauthorizedException, TaskListNotFoundException {
		taskListService.validateTaskList(taskListId);

		Status status = Status.from(statusString);
		String username = authUtil.getCurrentUsername();
		List<Task> tasks = taskRepository.findByTaskListIdAndStatus(taskListId, status, username);

		return tasks.stream().map(TaskResponse::from).toList();
	}

	/**
	 * Retrieves the task with the specified id in the specified task list.
	 * 
	 * @param taskListId the task list id.
	 * @param taskId     the task id.
	 * @return a DTO with the id, title, description and status of the task.
	 * @throws UnauthorizedException     when the user is not correctly
	 *                                   authenticated.
	 * @throws TaskListNotFoundException then the task list does not exist or is not
	 *                                   owned by the current user.
	 * @throws TaskNotFoundException     when the task does not exist or is not in
	 *                                   the specified task list.
	 */
	@Transactional(readOnly = true)
	public TaskResponse getTaskByIdFromTaskList(Long taskListId, Long taskId)
			throws UnauthorizedException, TaskListNotFoundException, TaskNotFoundException {
		taskListService.validateTaskList(taskListId);

		String username = authUtil.getCurrentUsername();
		Task task = taskRepository.findByIdAndTaskListId(taskId, taskListId, username)
				.orElseThrow(TaskNotFoundException::new);

		return TaskResponse.from(task);
	}

	/**
	 * Creates a new task and appends it to the specified task list.
	 * 
	 * @param taskListId the id of the task list.
	 * @param request    a DTO containing title, description and status of the new
	 *                   task.
	 * @return the id of the new task.
	 * @throws UnauthorizedException     when the user is not correctly
	 * @throws TaskListNotFoundException when the task list does no exist or is not
	 *                                   owned by the current user.
	 */
	@Transactional
	public Long createTaskForTaskList(Long taskListId, TaskRequest request)
			throws UnauthorizedException, TaskListNotFoundException {
		var status = Status.from(request.status());
		String username = authUtil.getCurrentUsername();
		Task task = taskRepository.save(
				request.title(),
				request.description(),
				status.toString(),
				taskListId,
				username)
				.orElseThrow(TaskListNotFoundException::new);

		return task.getId();
	}

	@Transactional
	public void updateTaskFromTaskList(Long taskListId, Long taskId, TaskRequest request)
			throws UnauthorizedException, TaskListNotFoundException, TaskNotFoundException {
		taskListService.validateTaskList(taskListId);

		String username = authUtil.getCurrentUsername();
		Task task = taskRepository.findByIdAndTaskListId(taskId, taskListId, username)
				.orElseThrow(TaskNotFoundException::new);

		if (request.title() != null)
			task.setTitle(request.title());

		if (request.description() != null)
			task.setDescription(request.description());

		if (request.status() != null) {
			var status = Status.from(request.status());
			task.setStatus(status);
		}

		taskRepository.save(task);
	}

	@Transactional
	public void updateTaskStatusFromTaskList(Long taskListId, Long taskId, String statusString)
			throws UnauthorizedException, TaskListNotFoundException, TaskNotFoundException {
		taskListService.validateTaskList(taskListId);

		Status status = Status.from(statusString);
		String username = authUtil.getCurrentUsername();
		Task task = taskRepository.findByIdAndTaskListId(taskId, taskListId, username)
				.orElseThrow(TaskNotFoundException::new);

		task.setStatus(status);
		taskRepository.save(task);
	}

	@Transactional
	public void deleteTaskFromTaskList(Long taskListId, Long taskId)
			throws UnauthorizedException, TaskListNotFoundException, TaskNotFoundException {
		taskListService.validateTaskList(taskListId);

		String username = authUtil.getCurrentUsername();
		taskRepository.deleteByIdAndTaskListId(taskId, taskListId, username)
				.orElseThrow(TaskNotFoundException::new);
	}
}
