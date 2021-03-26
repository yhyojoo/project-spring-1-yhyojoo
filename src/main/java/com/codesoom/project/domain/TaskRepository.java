package com.codesoom.project.domain;

import java.util.List;
import java.util.Optional;

/**
 * 할 일 저장소.
 */
public interface TaskRepository {
    List<Task> findAll();

    Optional<Task> findById(Long id);

    Task save(Task task);

    void delete(Task task);
}
