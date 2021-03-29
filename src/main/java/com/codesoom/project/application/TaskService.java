package com.codesoom.project.application;

import com.codesoom.project.domain.Task;
import com.codesoom.project.domain.TaskRepository;
import com.codesoom.project.dto.TaskCreateData;
import com.codesoom.project.dto.TaskUpdateData;
import com.codesoom.project.dto.TaskResultData;
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
    public Task getTask(Long id) {
        return findTask(id);
    }

    /**
     * 새로운 할 일을 등록합니다.
     *
     * @param taskCreateData 추가할 할 일 정보
     * @return 추가된 할 일
     * @throws TaskNotFoundException 할 일을 찾을 수 없을 경우
     */
    public TaskResultData createTask(
            TaskCreateData taskCreateData
    ) {
        Task task = taskCreateData.toEntity();

        taskRepository.save(task);

        return TaskResultData.of(task);
    }

    /**
     * 주어진 id에 해당하는 할 일을 수정합니다.
     *
     * @param id 할 일 식별자
     * @param taskUpdateData 수정할 할 일 정보
     * @return 수정된 할 일
     * @throws TaskNotFoundException 할 일을 찾을 수 없을 경우
     */
    public TaskResultData updateTask(
            Long id,
            TaskUpdateData taskUpdateData
    ) {
        Task task = findTask(id);

        task.updateWith(Task.builder()
                .title(taskUpdateData.getTitle())
                .build());

        return TaskResultData.of(task);
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
