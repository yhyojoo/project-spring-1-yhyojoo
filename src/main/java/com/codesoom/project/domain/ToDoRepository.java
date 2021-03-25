package com.codesoom.project.domain;

import java.util.List;
import java.util.Optional;

public interface ToDoRepository {
    List<ToDo> findAll();

    Optional<ToDo> findById(Long id);

    ToDo save(ToDo toDo);

    void delete(ToDo toDo);
}
