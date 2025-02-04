package io.github.caiomatenorio.tasklist_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import io.github.caiomatenorio.tasklist_service.model.Task;

public interface TaskRepository extends JpaRepository<Task, Long> {
}
