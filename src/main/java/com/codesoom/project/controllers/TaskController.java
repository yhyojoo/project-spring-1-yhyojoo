package com.codesoom.project.controllers;

import com.codesoom.project.application.TaskService;
import com.codesoom.project.domain.Task;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
