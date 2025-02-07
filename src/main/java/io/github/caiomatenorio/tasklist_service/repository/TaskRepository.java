package io.github.caiomatenorio.tasklist_service.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.lang.NonNull;

import io.github.caiomatenorio.tasklist_service.model.Status;
import io.github.caiomatenorio.tasklist_service.model.Task;
import jakarta.persistence.LockModeType;

public interface TaskRepository extends JpaRepository<Task, Long> {
	// The queries don't follow the name conventions because they are used to
	// prevent the access to other users' tasks. Use them instead of the
	// conventional to keep the tasks private.

	@Query("""
			SELECT task FROM Task task
			WHERE task.id = :id AND task.taskList.id = :taskListId AND task.taskList.user.username = :username
			""")
	Optional<Task> findByIdAndTaskListId(Long id, Long taskListId, String username);

	@Query("""
			SELECT task FROM Task task
			WHERE task.taskList.id = :taskListId AND task.taskList.user.username = :username
			""")
	List<Task> findByTaskListId(Long taskListId, String username);

	@Query("""
			SELECT task FROM Task task
			WHERE task.taskList.id = :taskListId AND task.status = :status AND task.taskList.user.username = :username
			""")
	List<Task> findByTaskListIdAndStatus(Long taskListId, Status status, String username);

	/**
	 * Persists a task to the database. Must be used within a transaction.
	 * 
	 * @param title       the title of the task.
	 * @param description the description of the task.
	 * @param status      the status of the task.
	 * @param taskListId  the id of the task list.
	 * @param username    the username of the owner
	 * @return an <code>Optional</code> with the persisted task if the task list
	 *         exists and it's owned by the user with the specified username.
	 * @return an empty <code>Optional</code> if the task list does not exist or
	 *         it's not owned by the user with the specified username.
	 */
	@Query(value = """
			INSERT INTO task (title, description, status, task_list_id)
			SELECT :title, :description, :status, tl.id
			FROM task_list tl JOIN app_user u ON u.id = tl.user_id
			WHERE tl.id = :taskListId
			AND u.username = :username
			FOR UPDATE
			RETURNING *;
			""", nativeQuery = true)
	Optional<Task> save(@NonNull String title, String description, @NonNull String status, @NonNull Long taskListId,
			@NonNull String username);

	@Override
	@Modifying
	@Lock(LockModeType.PESSIMISTIC_WRITE)
	@NonNull
	<S extends Task> S save(@NonNull S entity);

	@Query(value = """
			DELETE FROM task
			WHERE id = :taskId AND task_list_id IN (
				SELECT tl.id
				FROM task_list tl JOIN app_user u ON u.id = tl.user_id
				WHERE tl.id = :taskListId AND u.username = :username
			)
			RETURNING *;
			""", nativeQuery = true)
	Optional<Task> deleteByIdAndTaskListId(Long taskId, Long taskListId, String username);
}
