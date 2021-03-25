package com.codesoom.project.infra;

import com.codesoom.project.domain.ToDo;
import com.codesoom.project.domain.ToDoRepository;
import org.springframework.context.annotation.Primary;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

@Primary
public interface JpaToDoRepository
        extends ToDoRepository, CrudRepository<ToDo, Long> {
    List<ToDo> findAll();

    Optional<ToDo> findById(Long id);

    ToDo save(ToDo toDo);

    void delete(ToDo toDo);
}
