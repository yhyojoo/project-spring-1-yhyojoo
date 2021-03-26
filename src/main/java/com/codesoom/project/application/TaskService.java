package com.codesoom.project.application;

import com.codesoom.project.domain.Task;
import com.codesoom.project.domain.TaskRepository;
import com.codesoom.project.errors.TaskNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

/**
 * 할 일 관련 비즈니스 로직을 담당합니다.
 */
@Service
@Transactional
@RequiredArgsConstructor
public class TaskService {
    private final TaskRepository taskRepository;

    /**
     * 전체 할 일 목록을 반환합니다.
     *
     * @return 전체 할 일 목록
     */
    public List<Task> getTasks() {
        return taskRepository.findAll();
    }

    /**
     * 주어진 id에 해당하는 할 일을 반환합니다.
     *
     * @param id 할 일 식별자
     * @return 주어진 id를 갖는 할 일
     * @throws TaskNotFoundException 할 일을 찾을 수 없을 경우
     */
    public Task getTask(Long id) { return findTask(id);
    }

    /**
     * 주어진 id에 해당하는 할 일을 반환합니다.
     *
     * @param id 할 일 식별자
     * @return 주어진 id를 갖는 할 일
     */
    public Task findTask(Long id) {
        return taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException(id));
    }
}
