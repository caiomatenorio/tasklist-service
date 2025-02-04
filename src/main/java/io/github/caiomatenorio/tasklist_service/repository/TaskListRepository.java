package io.github.caiomatenorio.tasklist_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import io.github.caiomatenorio.tasklist_service.model.TaskList;

public interface TaskListRepository extends JpaRepository<TaskList, Long> {
}
