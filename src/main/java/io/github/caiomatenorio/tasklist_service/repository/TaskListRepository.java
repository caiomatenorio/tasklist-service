package io.github.caiomatenorio.tasklist_service.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

import io.github.caiomatenorio.tasklist_service.model.TaskList;
import jakarta.persistence.LockModeType;

public interface TaskListRepository extends JpaRepository<TaskList, Long> {
	// The queries don't follow the name conventions because they are used to
	// prevent the access to other users' task lists. Use them instead of the
	// conventional to keep the task lists private.

	@Query("""
			SELECT taskList from TaskList taskList
			WHERE taskList.user.username = :username
			""")
	List<TaskList> findAll(String username);

	@Query("""
			SELECT taskList from TaskList taskList
			WHERE taskList.id = :id AND taskList.user.username = :username
			""")
	Optional<TaskList> findById(Long id, String username);

	@Lock(LockModeType.PESSIMISTIC_READ)
	@Query("""
			SELECT CASE WHEN COUNT(taskList) > 0 THEN TRUE ELSE FALSE END FROM TaskList taskList
			WHERE taskList.id = :id AND taskList.user.username = :username
			""")
	boolean taskListExists(Long id, String username);
}
