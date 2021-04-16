package com.codesoom.project.controllers;

import com.codesoom.project.application.TaskService;
import com.codesoom.project.domain.Task;
import com.codesoom.project.dto.TaskCreateData;
import com.codesoom.project.dto.TaskUpdateData;
import com.codesoom.project.dto.TaskResultData;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

/**
 * 할 일 관련 요청을 처리합니다.
 */
@RestController
@RequestMapping("/diaries/{id}/tasks")
@CrossOrigin
@RequiredArgsConstructor
public class TaskController {
    private final TaskService taskService;

    /**
     * 전체 할 일 목록을 반환합니다.
     *
     * @return 전체 할 일 목록
     */
    @GetMapping
    public List<Task> list() {
        return taskService.getTasks();
    }

    /**
     * 주어진 id에 해당하는 할 일을 반환합니다.
     *
     * @param id 할 일 식별자
     * @return 주어진 id를 갖는 할 일
     */
    @GetMapping("{id}")
    public Task detail(@PathVariable Long id) {
        return taskService.getTask(id);
    }

    /**
     * 새로운 할 일을 추가합니다.
     *
     * @param taskCreateData 추가할 할 일 정보
     * @return 추가된 할 일
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TaskResultData create(
            @RequestBody @Valid TaskCreateData taskCreateData
    ) {
        return taskService.createTask(taskCreateData);
    }

    /**
     * 주어진 id에 해당하는 할 일을 수정합니다.
     *
     * @param id             할 일 식별자
     * @param taskUpdateData 수정할 할 일 정보
     * @return 수정된 할 일
     */
    @PatchMapping("{id}")
    @ResponseStatus(HttpStatus.OK)
    public TaskResultData update(@PathVariable Long id,
                                 @RequestBody @Valid TaskUpdateData taskUpdateData
    ) {
        return taskService.updateTask(id, taskUpdateData);
    }

    /**
     * 주어진 id에 해당하는 할 일을 삭제합니다.
     *
     * @param id 할 일 식별자
     */
    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.OK)
    public void delete(@PathVariable Long id) {
        taskService.deleteTask(id);
    }
}
